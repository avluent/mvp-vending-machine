@file:JvmName("ServerModule")
package com.josavezaat.vmachine.server

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.features.*
import io.ktor.auth.*
import io.ktor.sessions.*
import io.ktor.jackson.*

import com.josavezaat.vmachine.server.routes.*
import com.josavezaat.vmachine.common.*
import com.josavezaat.vmachine.data.*

import com.fasterxml.jackson.databind.SerializationFeature
import mu.KotlinLogging
import org.slf4j.event.*
import java.util.UUID
import java.io.File

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

fun Application.module() {
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
    install(ForwardedHeaderSupport)
    install(DefaultHeaders) {
        header(HttpHeaders.Server, "Avezaat MVP Vending Machine")
        header(HttpHeaders.UserAgent, "ktor")
        header(HttpHeaders.AccessControlAllowOrigin, "*")
    }
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
    install(Sessions) {
        cookie<ClientSession>(
            "mvp-session",
            directorySessionStorage(File(".session-data"), cached = true),
        )
    }
    install(Authentication) {
        basic("mvp-auth") {
            val logger = KotlinLogging.logger() {}
            realm = "Authentication for Jos Avezaat's Candy Machine"
            skipWhen { call ->
                call.sessions.get<ClientSession>() != null
            }
            validate { credentials ->

                logger.info("Generating new user session")

                /*
                // When testing
                if (this.request.headers.get("Testing") != "") {
                    this.request.call.sessions.set(
                        ClientSession(
                            "test",
                            2,
                            "Test",
                            "Bot",
                            Role.SELLER,
                            "bot@seller"
                        )
                    )
                    UserIdPrincipal("testBot")
                } */

                var currentSession: ClientSession? = null
                transaction {
                    Users.select { Users.userName eq credentials.name }
                        .andWhere { Users.password eq credentials.password }
                            .forEach { user ->
                                currentSession = ClientSession(
                                    UUID.randomUUID().toString(),
                                    user[Users.id],
                                    user[Users.firstName],
                                    user[Users.lastName],
                                    Role.valueOf(user[Users.role]),
                                    user[Users.userName]
                                )
                            }
                }

                this.request.call.sessions.set(currentSession!!)
                UserIdPrincipal(currentSession!!.userName)
            }
        }
    }
    routing {
        authenticate("mvp-auth") {
            route("/") {
                get {
                    call.respondText("Jos Avezaat's Vending Machine!")
                }
                get("/logout") {
                    call.sessions.clear<ClientSession>()
                    call.respondRedirect("/")
                }
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