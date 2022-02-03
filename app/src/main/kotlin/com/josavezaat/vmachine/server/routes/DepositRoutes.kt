@file:JvmName("DepositRoutes")
package com.josavezaat.vmachine.server.routes

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.auth.*
import io.ktor.sessions.*
import mu.KotlinLogging

import com.josavezaat.vmachine.common.*
import com.josavezaat.vmachine.data.*
import com.josavezaat.vmachine.application.CandyBarMachine

fun Route.depositRoutes() {

    val logger = KotlinLogging.logger() {}

    authenticate("mvp-auth") {

        route("/deposit") {

            post() {
                // get user ID from session cookie
                val session = call.sessions.get<ClientSession>()
                val userId = session?.id

                try {
                    if (userId == null)
                        throw Error("User not valid")

                    // get coin and deposit
                    val coinDeposit = call.receive<CoinDeposit>()
                    logger.debug(coinDeposit.toString())
                    val newDepositValue = CandyBarMachine.depositCoin(userId, coinDeposit.coin)

                    call.respond<ApiResponse>(
                        ApiResponse(
                            "Deposit successful. This is your new account balance!",
                            newDepositValue
                        )
                    )
                } catch (e: Exception) {

                    val message = "Error depositing coin: ${e}"
                    logger.error(message)
                    call.respond(ApiResponse(message))
                }

            }

            get("/reset") {

                // get user ID from session cookie
                val session = call.sessions.get<ClientSession>()
                val userId = session?.id

                try {
                    if (userId == null)
                        throw Error("User not valid")

                    CandyBarMachine.resetDeposit(userId)
                    call.respond<ApiResponse>(
                        ApiResponse(
                            "Your account's deposit was reset to 0.00"
                        )
                    )

                } catch(e: Exception) {

                    val message = "Error resetting deposit: ${e}"
                    logger.error(message)
                    call.respond(ApiResponse(message))
                }
            }
            
        }
    }


}
