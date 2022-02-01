@file:JvmName("EntryPoint")
package com.josavezaat.vmachine

import java.io.*
import io.ktor.application.*
import io.ktor.network.tls.certificates.*
import com.josavezaat.vmachine.server.*
import com.josavezaat.vmachine.data.*
import mu.KotlinLogging


fun main() {

    val logger = KotlinLogging.logger {}

    // Create mock database data
    logger.info("Creating mock users in database")
    createMockUsers()

    logger.info("Creating mock products in database")
    createMockProducts()

    // setup the cert for SSL
    logger.info("Generating SSL Certificate")
    generateCertificate(
        file = File("build/keystore.jks"),
        keyAlias = "mvp",
        keyPassword = "mvp",
        jksPassword = "mvp"
    )

    // start the server safely
    logger.info("Booting up server")
    try {

        ApiServer().instance.start(wait = true)

    } catch (e: Exception) {

        logger.error(e.toString())

    }

}
