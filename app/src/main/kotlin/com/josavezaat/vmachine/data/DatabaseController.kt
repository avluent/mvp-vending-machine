@file:JvmName("DatabaseController")
package com.josavezaat.vmachine.data

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

val prodAddress = "jdbc:postgresql://db"
val devAddress = "jdbc:postgresql://localhost:5432"

class DB(
    val address: String = devAddress,
    val database: String = "mvp",
    val driver: String = "org.postgresql.Driver",
    val user: String = "postgres",
    val password: String = "postgres"
) {

    fun connect(): Database {

        val fullDatabaseAddress = "$address/$database"

        return Database.connect(
            fullDatabaseAddress,
            driver = driver,
            user = user,
            password = password
        )

    }
}