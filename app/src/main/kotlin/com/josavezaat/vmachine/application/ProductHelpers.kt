@file:JvmName("ProductHelpers")
package com.josavezaat.vmachine.application

import com.josavezaat.vmachine.common.*
import com.josavezaat.vmachine.data.*

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

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

fun Int.isSeller(): Boolean {

    val _this = this
    logger.info("Checking whether the provided ID is indeed a seller")

    val userRole = transaction {
        Users.select { Users.id eq _this }
            .single()[Users.role]
    }

    if (userRole == "SELLER")
        return true

    return false
}