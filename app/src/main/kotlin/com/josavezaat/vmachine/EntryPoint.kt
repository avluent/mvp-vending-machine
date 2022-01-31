@file:JvmName("EntryPoint")
package com.josavezaat.vmachine

import java.io.*
import io.ktor.application.*
import io.ktor.network.tls.certificates.*
import com.josavezaat.vmachine.server.*
import mu.KotlinLogging

fun Application.main() {

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

        ApiServer().instance.start(wait = true)

    } catch (e: Exception) {

        logger.error(e.toString())

    }

}
