@file:JvmName("GenericClasses")
package com.josavezaat.vmachine.common

class ApiResponse(val message: String) {
    lateinit var value: Any
    constructor( message: String, value: Any): this(message) {
        this.value = value
    }
}

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
