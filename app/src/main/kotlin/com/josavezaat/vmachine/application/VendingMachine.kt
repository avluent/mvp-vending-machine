@file:JvmName("VendingMachine")
package com.josavezaat.vmachine.application

import com.josavezaat.vmachine.common.*
import com.josavezaat.vmachine.data.*
import mu.KotlinLogging

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transaction

object CandyBarMachine: VendingMachine {

    val logger = KotlinLogging.logger() {}
    override val purchases = mutableListOf<Purchase>()

    override fun buyProduct(purchase: Purchase): CustomerReceipt? {

        DB().connect()

        // check preconditions
        val productAmountAvailable: Int = 
            Products.select { Products.id eq purchase.productId }
                .single()[Products.amountAvailable]

        val buyersDepositAvailable: Double =
            Users.select { Users.id eq purchase.buyerId }
                .single()[Users.deposit]

        if (productAmountAvailable < purchase.productAmount 
            || buyersDepositAvailable < purchase.costTotal)
            return null

        // change product amount
        val newProductAmount = productAmountAvailable - purchase.productAmount
        Products.update({ Products.id eq purchase.productId }) {
            it[Products.amountAvailable] = newProductAmount
        }

        // withdraw amount from buyer deposit
        val newBuyersDeposit = buyersDepositAvailable - purchase.costTotal
        Users.update({ Users.id eq purchase.buyerId }) {
            it[Users.deposit] = newBuyersDeposit
        }

        // add amount to seller's deposit
        val currentSellersDeposit: Double =
            Users.select { Users.id eq purchase.sellerId }
                .single()[Users.deposit]
        val newSellersDeposit = currentSellersDeposit + purchase.costTotal
        Users.update({ Users.id eq purchase.sellerId }) {
            it[Users.deposit] = newSellersDeposit
        }

        // calculate change array from payed - amount
        val paymentSurplus = purchase.amountPayed - purchase.costTotal
        lateinit var change: List<Coin>
        if (paymentSurplus > 0)
            change = paymentSurplus.calculateChangeInCoins()
        else 
            change = listOf<Coin>()

        // return receipt
        val purchasedProductName = Products.select { Products.id eq purchase.productId }
            .single()[Products.name]

        return CustomerReceipt(
            purchase.costTotal,
            purchasedProductName,
            change
        )
    }

    // User functions
    override fun listUsers(): List<RegisteredUser> { 

        DB().connect()
        val userList = mutableListOf<RegisteredUser>()

        transaction {
            for (result in Users.selectAll()) {
                val user = RegisteredUser(
                    id = result[Users.id],
                    firstName = result[Users.firstName],
                    lastName = result[Users.lastName],
                    role = Role.valueOf(result[Users.role])
                )
                userList.add(user)
            }
        }

        logger.info(userList.toString())

        // return users
        return userList.toList()
    }

    override fun createUser(user: PrivateUser): RegisteredUser { 

        DB().connect()

        // create new user
        val newUser: Int = Users.insert {
            it[id] = user.id
            it[firstName] = user.firstName
            it[lastName] = user.lastName
            it[role] = user.role.toString()
            it[username] = user.username
            it[password] = user.password
            it[deposit] = user.deposit
        } get Users.id

        // verify if user was created
        lateinit var newlyCreatedUser: RegisteredUser
        transaction {
            for (result in Users.select { Users.id eq newUser }) {
                newlyCreatedUser = RegisteredUser(
                    result[Users.id],
                    result[Users.firstName],
                    result[Users.lastName],
                    Role.valueOf(result[Users.role])
                )
            }
        }

        return newlyCreatedUser
    }

    override fun updateUser(userId: Int, data: Map<String, Any>): PrivateUser? {

        DB().connect()

        // collect current user
        lateinit var currentUser: PrivateUser
        transaction {
            for (result in Users.select { Users.id eq userId }) {
                currentUser = PrivateUser(
                    result[Users.id],
                    result[Users.firstName],
                    result[Users.lastName],
                    Role.valueOf(result[Users.role]),
                    result[Users.username],
                    result[Users.password],
                    result[Users.deposit]
                )
            }
        }

        // patch current user with any
        val patchedUser = currentUser.patchWithMap(data)
        logger.debug("Patched user: ${patchedUser.toString()}")

        transaction {
            Users.update({ Users.id eq userId }) {
                it[firstName] = patchedUser.firstName
                it[lastName] = patchedUser.lastName
                it[role] = patchedUser.role.toString()
                it[username] = patchedUser.username
                it[password] = patchedUser.password
                it[deposit] = patchedUser.deposit
            }
        }

        // see if update was successful
        lateinit var updatedUser: PrivateUser
        transaction {
            for (result in Users.select { Users.id eq userId }) {
                updatedUser = PrivateUser(
                    result[Users.id],
                    result[Users.firstName],
                    result[Users.lastName],
                    Role.valueOf(result[Users.role]),
                    result[Users.username],
                    result[Users.password],
                    result[Users.deposit]
                )
            }
        }

        return updatedUser
    }

    override fun removeUser(userId: Int): Int { 
        return transaction {
            Users.deleteWhere { Users.id eq userId }
        }
    }

    // Deposit Functions
    override fun depositCoin(userId: Int, coin: Coin): Double {

        DB().connect()

        // collect coin and deposit value & combine
        val coinValue: Double = coin.getValue()
        val depositValue = Users.select { Users.id eq userId }.single()[Users.deposit]
        val newDepositValue = coinValue + depositValue

        // update user
        Users.update({ Users.id eq userId }) {
            it[deposit] = newDepositValue
        }

        return newDepositValue
    }

    override fun settleSellerCompensation(sellerId: Int, amount: Double): Double {

        DB().connect()

        // collect current deposit value and add
        val depositValue = Users.select { Users.id eq sellerId }.single()[Users.deposit]
        val newDepositValue = depositValue + amount

        // update user
        Users.update( { Users.id eq sellerId }) {
            it[deposit] = newDepositValue
        }

        return newDepositValue
    }

    override fun resetDeposit(userId: Int): Int {

        DB().connect()

        // reset deposit for user to zero
        return Users.update( { Users.id eq userId }) {
            it[deposit] = 0.00
        }
    }

    // Product functions
    override fun listProducts(): List<PresentableProduct> {

        DB().connect()

        val productList = mutableListOf<PresentableProduct>()

        for (result in Products.selectAll()) {
            val product = PresentableProduct(
                result[Products.id],
                result[Products.name],
                result[Products.amountAvailable],
                result[Products.cost]
            )
            productList.add(product)
        }

        return productList.toList()
    }

    override fun createProduct(product: FullProduct): PresentableProduct? {

        DB().connect()

        // check if product cost is multiples of 5
        if (!product.cost.decimalIsMultipleOfFive())
            return null

        // insert product
        val newProduct: Int = Products.insert {
            it[name] = product.name
            it[amountAvailable] = product.amountAvailable
            it[cost] = product.cost
            it[sellerId] = product.sellerId
        } get Products.id

        // check if insertion succeeded
        lateinit var newlyCreatedProduct: PresentableProduct
        for (result in Products.select { Products.id eq newProduct }) {
            newlyCreatedProduct = PresentableProduct(
                result[Products.id],
                result[Products.name],
                result[Products.amountAvailable],
                result[Products.cost]
            )
        }

        return newlyCreatedProduct
    }

    override fun updateProduct(productId: Int, data: Map<String, Any>): FullProduct? {

        DB().connect()

        /*
        // check if product cost is multiples of 5
        if (!data.cost.decimalIsMultipleOfFive())
            return null

        // perform update
        Products.update({ Products.id eq productId }) { 
            it[name] = data.name
            it[amountAvailable] = data.amountAvailable
            it[cost] = data.cost
            it[sellerId] = data.sellerId
        }

        // see if update was successful
        lateinit var updatedProduct: FullProduct
        for (result in Products.select { Products.id eq productId }) {
            updatedProduct = FullProduct(
                result[Products.id],
                result[Products.name],
                result[Products.amountAvailable],
                result[Products.cost],
                result[Products.sellerId]
            )
        }

        return updatedProduct
        */
        return null
    }

    override fun removeProduct(productId: Int): Int {
        return Products.deleteWhere { Products.id eq productId }
    }

}