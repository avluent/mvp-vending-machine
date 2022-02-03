@file:JvmName("CreateUsers")
package com.josavezaat.vmachine.data

import com.josavezaat.vmachine.common.*

import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.dao.id.*

object Users: Table("USERS") {
    val id: Column<Int> = integer("id").autoIncrement()
    val firstName: Column<String> = varchar("first_name", 50)
    val lastName: Column<String> = varchar("last_name", 50)
    val role: Column<String> = varchar("role", 50)
    val userName: Column<String> = varchar("username", 50)
    val password: Column<String> = varchar("password", 50)
    val deposit: Column<Double> = double("deposit")
}

val mockUsers: List<PrivateUser> = listOf(
    PrivateUser(
        1,
        "Joe",
        "Packson",
        Role.BUYER,
        "joe.packs@on.com",
        "jlksjdfs98734",
        0.00
    ),
    PrivateUser(
        2,
        "Marina",
        "Rogan",
        Role.SELLER,
        "marina.roe@on.com",
        "kdjf83",
        2.20
    )
)

fun createMockUsers() {

    DB().connect()
    transaction {

        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Users)

        for (user in mockUsers) {
            Users.insert {
                // it[id] = user.id
                it[firstName] = user.firstName
                it[lastName] = user.lastName
                it[role] = user.role.toString()
                it[userName] = user.userName
                it[password] = user.password
                it[deposit] = user.deposit
            }
        }
    }
}