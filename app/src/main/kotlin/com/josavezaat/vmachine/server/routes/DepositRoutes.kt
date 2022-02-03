@file:JvmName("DepositRoutes")
package com.josavezaat.vmachine.server.routes

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.response.*
import io.ktor.auth.*

import com.josavezaat.vmachine.common.*

fun Route.depositRoutes() {

    authenticate("mvp-auth") {

        route("/deposit") {

            get() {

                // get user ID from session cookie

                // mock the user's deposit
                call.respond<ApiResponse>(
                    ApiResponse(
                        "This is your deposit account statement",
                        4.25
                    )
                )

            }

            get("/reset") {

                // mock user id from session cookie
                val userId = "user-992"

                // get user ID from session cookie
                call.respond<ApiResponse>(
                    ApiResponse(
                        "Deposit for user ${userId} was successfully reset",
                        0.00
                    )
                )

            }
            
        }
    }


}
