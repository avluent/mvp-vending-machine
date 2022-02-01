@file:JvmName("ProductRoutes")
package com.josavezaat.vmachine.server.routes

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.response.*

import com.josavezaat.vmachine.common.*

class PresentableProduct(
    override val id: Int,
    override var name: String,
    override var amountAvailable: Int,
    override var cost: Double,
): Product

class FullProduct(
    override val id: Int,
    override var name: String,
    override var amountAvailable: Int,
    override var cost: Double,
    override var sellerId: Int
): Product, PrivateProductData

fun Route.productRoutes() {

    route("/products") {

        get() {

            // mock list of products
            call.respond<List<PresentableProduct>>(
                listOf(
                    PresentableProduct(
                        22,
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
                    12,
                    "Kixx",
                    15,
                    1.50,
                    19
                )
            )
        }

        patch("/update/{productId}") {

            val productId = call.parameters["productId"]

            // mock the updated response
            call.respond<FullProduct>(
                FullProduct(
                    83,
                    "Soundy",
                    8,
                    1.20,
                    22
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

