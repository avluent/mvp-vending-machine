@file:JvmName("UserRoutes")
package com.josavezaat.vmachine.server.routes

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.response.*

import com.josavezaat.vmachine.common.*

class RegisteredUser(
    override val id: Int,
    override var firstName: String,
    override var lastName: String,
    override var role: Role
): User

class PrivateUser(
    override val id: Int,
    override var firstName: String,
    override var lastName: String,
    override var role: Role,
    override var username: String,
    override var password: String,
    override var deposit: Double
): User, PrivateUserData

fun Route.userRoutes() {

    route("/users") {

        get() {

            // return a list of all registered users
            // mock for testing
            call.respond<List<RegisteredUser>>(
                listOf(
                    RegisteredUser(
                        18,
                        "Jamie",
                        "Rockafeller",
                        Role.BUYER
                    )
                )
            )

        }

        post("/create") {

            // mock the newly created user
            call.respond<RegisteredUser>(
                RegisteredUser(
                    29,
                    "New", 
                    "Userino", 
                    Role.SELLER
                )
            )
        }

        patch("/update/{userId}") {

            // takes in the id of the user to change
            // mock the updated user
            call.respond<PrivateUser>(
                PrivateUser(
                    11,
                    "Updeed",
                    "Usree",
                    Role.BUYER,
                    "554@buyer.com",
                    "IlikeToBuy",
                    82.53
                )
            )
        }

        delete("/delete/{userId}") {

            val id: String? = call.parameters["userId"]

            // should no id have been provided
            if (id === null) {
                val message = "Please insert an id: /delete/{id}"
                call.respond<ApiResponse>(
                    ApiResponse(message)
                )

            }

            // mock the removed user
            val message = "User with id: ${id} was successfully removed."
            call.respond<ApiResponse>(
                ApiResponse(message)
            )
                
        }

    }


}


