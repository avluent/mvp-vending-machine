@file:JvmName("VendingMachineRoutes")
package com.josavezaat.vmachine.server.routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

import com.josavezaat.vmachine.common.*

class CustomerReceipt(
    override val totalSpent: Double,
    override val purchasedProduct: String,
    override val change: List<Coin>
): Receipt 

class ProcessedPurchase(
    override val sellerId: String,
    override val buyerId: String,
    override val productId: String,
    override val productAmount: Int,
    override val costTotal: Double
): Purchase 

fun Route.vendingMachineRoutes() {

    get("/purchases") {

        // Processed purchase mock
        call.respond<List<ProcessedPurchase>>(
            listOf<ProcessedPurchase>(
                ProcessedPurchase(
                    "Seller-1", 
                    "Buyer-1",
                    "Product-1", 
                    12, 
                    19.50)
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
