@file:JvmName("UserHelpers")
package com.josavezaat.vmachine.application

import mu.KotlinLogging
import com.josavezaat.vmachine.common.*
import com.josavezaat.vmachine.data.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

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
                "userName" -> this.userName = prop.value.toString()
                "password" -> this.password = prop.value.toString()
                "deposit" -> this.deposit = prop.value.toString().toDouble()
            }
    }

    return this
}
