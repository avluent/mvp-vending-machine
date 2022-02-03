@file:JvmName("PurchaseHelpers")
package com.josavezaat.vmachine.application

import com.josavezaat.vmachine.common.*
import com.josavezaat.vmachine.data.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

fun Double.calculateChangeInCoins(): List<Coin> {
    
    var amountLeft: Double = this.round(2)

    val coinList = mutableListOf<Coin>()
    val doubleParts: List<Int> = this.splitToInt()

    // add 100ct coins prior to separator
    while (amountLeft > 0.00) {
        logger.debug("Looping Amount left: ${amountLeft.toString()}")
        when {
            amountLeft > 1.00 -> {
                coinList.add(Coin.HUNDREDCENTS)
                amountLeft -= 1.00
            }
            amountLeft > 0.50 -> {
                coinList.add(Coin.FIFTYCENTS)
                amountLeft -= 0.50
            }
            amountLeft > 0.20 -> {
                coinList.add(Coin.TWENTYCENTS)
                amountLeft -= 0.20
            }
            amountLeft > 0.10 -> {
                coinList.add(Coin.TENCENTS)
                amountLeft -= 0.10
            }
            amountLeft > 0.05 -> {
                coinList.add(Coin.FIVECENTS)
                amountLeft -= 0.05
            }
            else -> {
                amountLeft = 0.00 // due to double inaccuracy
            }
        }
    }

    return coinList.toList()
}


fun NewPurchase.addPurchaseMetadata(buyerId: Int): ProcessedPurchase? {

    val purchase = ProcessedPurchase(
        sellerId = 0, // init to 0
        buyerId = buyerId, // this user's id -- set with session !!
        productId = this.productId,
        productAmount = this.productAmount,
        costTotal = 0.00, // init to 0
        amountPayed = this.amountPayed,
        productAmountAvailable = 0,
        buyersDepositAvailable = 0.00
    )

    transaction {
        for (result in Products.select { Products.id eq purchase.productId }) {
            purchase.costTotal = (result[Products.cost] * purchase.productAmount)
                .toDouble()
                .round(2)
            purchase.sellerId = result[Products.sellerId].toInt()
        }
    }

    purchase.addPreconditions()

    logger.info("Metadata added to purchase")
    return purchase

}

fun ProcessedPurchase.addPreconditions(): ProcessedPurchase? {

    val _this = this

    val productAmountAvailable: Int = 
        transaction{
            Products.select { Products.id eq _this.productId }
                .single()[Products.amountAvailable]
        }
    
    val buyersDepositAvailable: Double =
        transaction {
            Users.select { Users.id eq _this.buyerId }
                .single()[Users.deposit]
        }.round(2)

    if (productAmountAvailable < this.productAmount 
        || buyersDepositAvailable < this.costTotal)
        return null

    return this
}

fun ProcessedPurchase.settleTransaction(): ProcessedPurchase {

    val _this = this

    // withdraw amount from buyer deposit
    val newBuyersDeposit = (this.buyersDepositAvailable - this.costTotal)
        .round(2)

    transaction {
        Users.update({ Users.id eq _this.buyerId }) {
                it[Users.deposit] = newBuyersDeposit
        }
    }

    // add amount to seller's deposit
    val currentSellersDeposit: Double =
        transaction {
            Users.select { Users.id eq _this.sellerId}
                .andWhere { Users.role eq "SELLER" }
                    .single()[Users.deposit]
        }.round(2)

    val newSellersDeposit = currentSellersDeposit + this.costTotal
    transaction {
        Users.update({ Users.id eq _this.sellerId }) {
            it[Users.deposit] = newSellersDeposit
        }
    }

    logger.info("Transaction settled")
    return this
}
