@file:JvmName("EntryPoint")
package com.josavezaat.vmachine

import java.io.*
import io.ktor.network.tls.certificates.*
import com.josavezaat.vmachine.server.*
import mu.KotlinLogging

fun main() {

    val logger = KotlinLogging.logger {}

    // setup the cert for SSL
    generateCertificate(
        file = File("build/keystore.jks"),
        keyAlias = "mvp",
        keyPassword = "mvp",
        jksPassword = "mvp"
    )

    // start the server safely
    try {

        ApiServer().start

    } catch (e: Exception) {

        logger.error(e.toString())

    }

}
