@file:JvmName("TestingCommon")
package com.josavezaat.vmachine

import com.fasterxml.jackson.databind.*
import io.ktor.auth.UserPasswordCredential
import java.util.Base64
import mu.KotlinLogging

val logger = KotlinLogging.logger() {}

// json s11n
fun <T> T.toJson(): String {

    val om = ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)

    return om.writeValueAsString(this)
}

fun getTestingCredentials(): String {
    
    // test user
    val userName = "marina.roe@on.com"
    val password = "kdjf83"

    val credentialString = Base64
        .getEncoder()
        .encodeToString(
            "${userName}:${password}"
                .toByteArray()
            )

    val headerString = "Basic ${credentialString}"

    logger.debug(headerString)

    return headerString
}