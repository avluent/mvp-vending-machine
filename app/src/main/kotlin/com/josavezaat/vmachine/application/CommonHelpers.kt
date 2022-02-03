@file:JvmName("CommonHelpers")
package com.josavezaat.vmachine.application

import mu.KotlinLogging
import com.josavezaat.vmachine.common.*
import com.josavezaat.vmachine.data.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

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

fun Double.round(decimals: Int = 2): Double = 
    "%.${decimals}f".format(this).toDouble()
