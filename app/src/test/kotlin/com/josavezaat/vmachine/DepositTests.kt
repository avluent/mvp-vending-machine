@file:JvmName("DepositTests")
package com.josavezaat.vmachine

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.testing.*
import kotlin.test.*

import com.josavezaat.vmachine.server.*
import com.josavezaat.vmachine.common.*
import com.josavezaat.vmachine.*

class DepositRouteTest {

    // @Test
    fun testDepositRoot() {
        withTestApplication( Application::module ) { 
            handleRequest(HttpMethod.Post, "/api/deposit", setup = {
                addHeader("Authorization", getTestingCredentials())
                addHeader("Testing", "true")
            }).apply { 
                cookiesSession { 
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals(ApiResponse(
                        "This is your deposit account statement",
                        4.25
                    ).toJson(), response.content)
                }
            }
        }
    }
}