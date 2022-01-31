@file:JvmName("ServerConfiguration")
package com.josavezaat.vmachine.server

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.features.*
import io.ktor.auth.*
import io.ktor.sessions.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.jackson.*

import com.josavezaat.vmachine.server.routes.*

import java.io.*
import java.security.KeyStore
import kotlin.math.log
import com.fasterxml.jackson.databind.SerializationFeature
import mu.KotlinLogging
import org.slf4j.event.*

class ApiServer {

    private val keystoreFilePath = "build/keystore.jks"
    private val keyAlias = "mvp"
    private val keystorePassword = "mvp"
    private val privateKeyPassword = "mvp"

    private val logger = KotlinLogging.logger {}

    private val serverEnvironment = applicationEngineEnvironment {

        sslConnector(
            loadKeystore(keystoreFilePath, keystorePassword)!!,
            keyAlias,
            {keystorePassword.toCharArray()},
            {privateKeyPassword.toCharArray()}
        ) {
            port = 8443
            module {
                install(CORS) {
                    method(HttpMethod.Get)
                    method(HttpMethod.Post)
                    method(HttpMethod.Options)
                    method(HttpMethod.Patch)
                    method(HttpMethod.Delete)
                    maxAgeInSeconds = 3600
                    allowHeaders { true }
                    allowCredentials = true
                }
                install(IgnoreTrailingSlash)
                install(DefaultHeaders) {
                    header(HttpHeaders.Server, "Avezaat MVP Vending Machine")
                    header(HttpHeaders.UserAgent, "ktor")
                    header(HttpHeaders.AccessControlAllowOrigin, "*")
                }
                install(ForwardedHeaderSupport)
                install(Compression) {
                    gzip {
                        matchContentType(
                            ContentType.Text.Any,
                            ContentType.Application.JavaScript
                        )
                    }
                }
                install(CallLogging) {
                    level = Level.INFO
                }
                install(ContentNegotiation) {
                    jackson {
                        enable(SerializationFeature.INDENT_OUTPUT)
                    }
                }
                routing {
                    route("/") {
                        get {
                            call.respondText("Jos Avezaat's Vending Machine!")
                        }
                    }
                    route("/api") {
                        vendingMachineRoutes()
                        userRoutes()
                        productRoutes()
                        depositRoutes()
                    }
                }
            }
        }

    }

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

    val start = embeddedServer(Netty, serverEnvironment).start(wait = true)
}