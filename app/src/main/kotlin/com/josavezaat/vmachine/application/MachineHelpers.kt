@file:JvmName("MachineHelpers")
package com.josavezaat.vmachine.application

import mu.KotlinLogging
import com.josavezaat.vmachine.common.*

val logger = KotlinLogging.logger() {}

fun Double.splitToInt(): List<Int> {

    // convert to string and split
    val thisAsString = this.toString()
    val doubleParts = thisAsString.split(".")

    return doubleParts.map { it.toInt() }

}

fun Double.decimalIsMultipleOfFive(): Boolean {
    val divided: Double = this / 0.05

    if (divided % 1 == 0.0) {
        logger.debug((divided % 1).toString())
        return true

    }
    else {
        return false

    }
}

fun Double.calculateChangeInCoins(): List<Coin> {
    
    var amountLeft: Double = this

    val coinList = mutableListOf<Coin>()
    val doubleParts: List<Int> = this.splitToInt()
    val intValue: Int = doubleParts[0]

    // add 100ct coins prior to separator
    repeat(intValue) {
        coinList.add(Coin.HUNDREDCENTS)
        amountLeft -= 1.00
    }

    while (amountLeft > 0.00) {
        when {
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
            amountLeft > 1.00 -> logger.info("Still too much. Do nothing.")
            else -> {
                logger.error("Unexpected case when handing out change.")
            }
        }
    }

    return coinList.toList()
}

fun PrivateUser.patchWithMap(patch: Map<String, Any>): PrivateUser {

    logger.info("Now patching user with map")

    val props = this.javaClass.getDeclaredFields()
    val propNames = props.map { it.getName() }

    for (prop in patch) {
        if (propNames.contains(prop.key))
            when (prop.key) {
                "firstName" -> this.firstName = prop.value.toString()
                "lastName" -> this.lastName = prop.value.toString()
                "role" -> this.role = Role.valueOf(prop.value.toString())
                "username" -> this.username = prop.value.toString()
                "password" -> this.password = prop.value.toString()
                "deposit" -> this.deposit = prop.value.toString().toDouble()
            }
    }

    return this
}

fun FullProduct.patchWithMap(patch: Map<String, Any>): FullProduct {

    logger.info("Now patching product with map")

    val props = this.javaClass.getDeclaredFields()
    val propNames = props.map { it.getName() }

    for (prop in patch) {
        if (propNames.contains(prop.key))
            when (prop.key) {
                "name" -> this.name = prop.value.toString()
                "amountAvailable" -> this.amountAvailable = prop.value.toString().toInt()
                "cost" -> this.cost = prop.value.toString().toDouble()
                "sellerId" -> this.sellerId = prop.value.toString().toInt()
            }
    }

    return this
}