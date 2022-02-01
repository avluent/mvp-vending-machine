@file:JvmName("Enums")
package com.josavezaat.vmachine.common

enum class Role {
    BUYER, SELLER
}

enum class Coin(value: Double) {
    FIVECENTS(0.05),
    TENCENTS(0.10),
    TWENTYCENTS(0.20),
    FIFTYCENTS(0.50),
    HUNDREDCENTS(1.00),
}

fun Coin.getValue(): Double {
    when (this.ordinal) {
        0 -> return 0.05
        1 -> return 0.10
        2 -> return 0.20
        3 -> return 0.50
        4 -> return 1.00
        else -> return 0.00
    }
}

