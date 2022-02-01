@file:JvmName("VendingMachineRoutes")
package com.josavezaat.vmachine.server.routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

import com.josavezaat.vmachine.common.*

fun Route.vendingMachineRoutes() {

    get("/purchases") {

        // Processed purchase mock
        call.respond<List<ProcessedPurchase>>(
            listOf<ProcessedPurchase>(
                ProcessedPurchase(12, 3, 5, 12, 19.50, 20.00)
            )
        )

    }

    post("/buy") {

        // input must be purchase with only product and amount
        // add metadata to purchase

        // return receipt mock
        call.respond<CustomerReceipt>(
            CustomerReceipt(
                0.05, 
                "Silky Way", 
                listOf(
                    Coin.FIVECENTS
                ))
        )

    }
}
