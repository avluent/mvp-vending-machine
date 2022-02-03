@file:JvmName("ProductRoutes")
package com.josavezaat.vmachine.server.routes

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.request.*
import io.ktor.request.receive
import io.ktor.response.*
import io.ktor.auth.*
import mu.KotlinLogging
import kotlin.error

import com.josavezaat.vmachine.common.*
import com.josavezaat.vmachine.application.CandyBarMachine

fun Route.productRoutes() {

    val logger = KotlinLogging.logger() {}

    authenticate("mvp-auth") {

        route("/products") {

            get() {
                try {
                    val productList = CandyBarMachine.listProducts()
                    call.respond<List<PresentableProduct>>(productList)

                } catch(e: Exception) {

                    val message = "Error retrieving products: ${e}"
                    logger.error(message)
                    call.respond(ApiResponse(message))
                }
            }

            post("/create") {
                try {
                    val product = call.receive<FullProduct>()

                    val createdProduct = CandyBarMachine.createProduct(product)
                    if (createdProduct === null)
                        throw Error("Created product was not returned from database")

                    call.respond<PresentableProduct>(createdProduct)

                } catch(e: Exception) {

                    val message = "Error creating product: ${e}"
                    logger.error(message)
                    call.respond(ApiResponse(message))
                }
            }

            patch("/update/{productId}") {
                try {
                    val productId: String? = call.parameters["productId"]
                    if (productId === null)
                        throw Error("Product ID Provided was either wrong or missing.")

                    val data = call.receive<Map<String, Any>>()

                    val updatedProduct: FullProduct? = 
                        CandyBarMachine.updateProduct(productId.toInt(), data)

                    if (updatedProduct == null)
                        throw Error("Patch did not return a valid product")

                    call.respond<FullProduct>(updatedProduct)

                } catch (e: Exception) {

                    val message = "Error updating product: ${e}"
                    logger.error(message)
                    call.respond(ApiResponse(message))
                }
            }

            delete("/delete/{productId}") {
                try {
                    val productId: String? = call.parameters["productId"]
                    if (productId === null)
                        throw Error("Please insert an ID -> /delete/{productId}")
                    
                    CandyBarMachine.removeProduct(productId.toInt())

                    val message = "Successfully removed product with id: ${productId}."
                    call.respond(ApiResponse(message))
                } catch (e: Exception) {

                    val message = "Error deleting product: ${e}"
                    logger.error(message)
                    call.respond(ApiResponse(message))
                }
            }
        }
    }
}

