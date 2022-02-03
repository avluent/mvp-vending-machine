@file:JvmName("VendingMachineRoutes")
package com.josavezaat.vmachine.server.routes

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

import com.josavezaat.vmachine.common.*
import com.josavezaat.vmachine.application.*
import mu.KotlinLogging

fun Route.vendingMachineRoutes() {

    val logger = KotlinLogging.logger() {}

    get("/purchases") {
        val transactionList = CandyBarMachine.purchases.toList()
        call.respond<List<ProcessedPurchase>>(transactionList)
    }

    post("/buy") {
        try {

            val purchase = call.receive<NewPurchase>()
            val receipt = CandyBarMachine.buyProduct(purchase)

            if (receipt == null)
                throw Error("Receipt was not created. Invalid purchase!")

            call.respond<CustomerReceipt>(receipt)

        } catch (e: Exception) {

            val message = "Error with printing your receipt: ${e}"
            logger.error(message)
            call.respond(ApiResponse(message))

        }
    }
}
