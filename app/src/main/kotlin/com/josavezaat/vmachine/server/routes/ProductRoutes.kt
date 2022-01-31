@file:JvmName("ProductRoutes")
package com.josavezaat.vmachine.server.routes

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.response.*

import com.josavezaat.vmachine.common.*

open class PresentableProduct(
    override val id: String,
    override var name: String,
    override var amountAvailable: Int,
    override var cost: Double,
    override val sellerId: String? = null
): Product

class FullProduct(
    override val id: String,
    override var name: String,
    override var amountAvailable: Int,
    override var cost: Double,
    override val sellerId: String
): PresentableProduct(id, name, amountAvailable, cost)

fun Route.productRoutes() {

    route("/products") {

        get() {

            // mock list of products
            call.respond<List<PresentableProduct>>(
                listOf(
                    PresentableProduct(
                        "prod-123",
                        "Kixx",
                        15,
                        1.50
                    )
                )
            )

        }

        post("/create") {

            // only accept FULL Product
            // mock the newly created product
            call.respond<FullProduct>(
                FullProduct(
                    "prod-123",
                    "Kixx",
                    15,
                    1.50,
                    "sellr-432"
                )
            )
        }

        patch("/update/{productId}") {

            val productId = call.parameters["productId"]

            // mock the updated response
            call.respond<FullProduct>(
                FullProduct(
                    "prod-559",
                    "Soundy",
                    8,
                    1.20,
                    "sellr-128"
                )
            )
        }

        delete("/delete/{productId}") {

            val productId = call.parameters["productId"]

            // mock the deleted response
            call.respond<ApiResponse>(
                ApiResponse(
                    "Product ${productId} was successfully removed"
                )
            )
        }
    }

}

