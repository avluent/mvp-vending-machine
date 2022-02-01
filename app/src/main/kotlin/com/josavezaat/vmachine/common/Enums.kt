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
    HUNDREDCENTS(1.00)
}

