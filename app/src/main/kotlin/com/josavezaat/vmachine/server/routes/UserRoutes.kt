@file:JvmName("UserRoutes")
package com.josavezaat.vmachine.server.routes

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode
import mu.KotlinLogging

import com.josavezaat.vmachine.common.*
import com.josavezaat.vmachine.application.*

fun Route.userRoutes() {

    val logger = KotlinLogging.logger() {}

    route("/users") {

        // creating users is not authenticated
        post("/create") {
            try {
                val user = call.receive<PrivateUser>()
                val createdUser = CandyBarMachine.createUser(user)

                if (createdUser == null) {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respond(ApiResponse(
                        "Username already exists"
                    ))
                } else {

                    call.respond<RegisteredUser>(createdUser)
                }

            } catch (e: Exception) {

                val message = "Error creating user: ${e}"
                logger.error(message)
                call.respond(ApiResponse(message))
            }
            
        }

        authenticate("mvp-auth") {

            get() {
                try {
                    val userList = CandyBarMachine.listUsers()
                    call.respond<List<RegisteredUser>>(userList)

                } catch (e: Exception) {

                    val message = "User list could not be retrieved: ${e}"
                    logger.error(message)
                    call.respond(ApiResponse(message))
                }
            }

            patch("/update/{userId}") {
                try {
                    val userId: Int? = call.parameters["userId"]?.toInt()
                    if (userId === null)
                        throw Error("User ID provided was not correct or missing.")

                    // object to patch with
                    val data = call.receive<Map<String, Any>>()

                    val updatedUser: PrivateUser? = CandyBarMachine.updateUser(userId, data)
                    if (updatedUser === null)
                        throw Error("Patch did not return a valid user.")

                    call.respond<PrivateUser>(updatedUser)

                } catch (e: Exception) {

                    val message = "There was an error updating the user: ${e}"
                    logger.error(message)
                    call.respond(ApiResponse(message))
                }
            }

            delete("/delete/{userId}") {

                try {

                    val id: String? = call.parameters["userId"]

                    // should no id have been provided
                    if (id === null)
                        throw Error("Please insert an id: /delete/{id}")

                    CandyBarMachine.removeUser(id.toInt())

                    val message = "User with id: ${id} was successfully removed."
                    call.respond<ApiResponse>(ApiResponse(message))

                } catch (e: Exception) {

                    val message = "Error deleting user: ${e}"
                    logger.error(message)
                    call.respond(ApiResponse(message))

                }
            }
        }
    }
}


