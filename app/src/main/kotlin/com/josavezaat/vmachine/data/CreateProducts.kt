@file:JvmName("CreateProducts")
package com.josavezaat.vmachine.data

import com.josavezaat.vmachine.common.*

import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.dao.id.*

object Products: Table("PRODUCTS") {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 50)
    val amountAvailable: Column<Int> = integer("amount_available")
    var cost: Column<Double> = double("cost")
    val sellerId: Column<Int> = integer("seller_id")
}

class FullProduct(
    override val id: Int,
    override var name: String,
    override var amountAvailable: Int,
    override var cost: Double,
    override var sellerId: Int
): Product, PrivateProductData

val mockProducts: List<FullProduct> = listOf(
    FullProduct(
        0,
        "Slix",
        5,
        1.20,
        1
    ),
    FullProduct(
        1,
        "Pars",
        15,
        1.00,
        1
    ),
    FullProduct(
        2,
        "Dounty",
        8,
        0.85,
        1
    ),
    FullProduct(
        3,
        "Silky Stay",
        4,
        1.15,
        1
    ),
)

fun createMockProducts() {

    DB().connect()
    transaction {

        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Products)

        for (product in mockProducts) {
            Products.insert {
                it[id] = product.id
                it[name] = product.name
                it[amountAvailable] = product.amountAvailable
                it[cost] = product.cost
                it[sellerId] = product.sellerId
            }
        }
    }
}
