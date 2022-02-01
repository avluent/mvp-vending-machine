@file:JvmName("ServerInstance")
package com.josavezaat.vmachine.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

import java.io.*
import java.security.KeyStore
import mu.KotlinLogging
import org.slf4j.event.*

class ApiServer {

    private val keystoreFilePath = "build/keystore.jks"
    private val keyAlias = "mvp"
    private val keystorePassword = "mvp"
    private val privateKeyPassword = "mvp"

    private val logger = KotlinLogging.logger {}

    private val serverEnvironment = applicationEngineEnvironment {

        log = logger

        sslConnector(
            loadKeystore(keystoreFilePath, keystorePassword)!!,
            keyAlias,
            {keystorePassword.toCharArray()},
            {privateKeyPassword.toCharArray()}
        ) {
            port = 8443
        }

        module(Application::module)
    }

    val instance = embeddedServer(Netty, serverEnvironment)

    private fun loadKeystore(
        keystoreFilePath: String,
        keystorePassword: String,
    ): KeyStore? {
        
        try {

            val fileInputStream = File(keystoreFilePath).inputStream()
            
            val keystore = KeyStore.getInstance("jks")
            keystore.load(fileInputStream, keystorePassword.toCharArray())

            return keystore

        } catch (e: Exception) {

            logger.error { "Could not load Keystore" }
            return null

        }

    }
}