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
import com.fasterxml.jackson.databind.SerializationFeature
import mu.KotlinLogging
import org.slf4j.event.*


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