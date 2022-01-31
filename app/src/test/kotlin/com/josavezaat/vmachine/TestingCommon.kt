@file:JvmName("TestingCommon")
package com.josavezaat.vmachine

import com.fasterxml.jackson.databind.*

// json s11n
fun <T> T.toJson(): String {

    val om = ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)

    return om.writeValueAsString(this)
}
