@file:JvmName("GenericClasses")
package com.josavezaat.vmachine.common

class ApiResponse(val message: String) {
    lateinit var value: Any
    constructor( message: String, value: Any): this(message) {
        this.value = value
    }
}

data class RegisteredUser(
    override val id: Int,
    override var firstName: String,
    override var lastName: String,
    override var role: Role
): User

data class PrivateUser(
    override val id: Int,
    override var firstName: String,
    override var lastName: String,
    override var role: Role,
    override var username: String,
    override var password: String,
    override var deposit: Double
): User, PrivateUserData

data class PresentableProduct(
    override val id: Int,
    override var name: String,
    override var amountAvailable: Int,
    override var cost: Double,
): Product

data class FullProduct(
    override val id: Int,
    override var name: String,
    override var amountAvailable: Int,
    override var cost: Double,
    override var sellerId: Int
): Product, PrivateProductData

data class CustomerReceipt(
    override val totalSpent: Double,
    override val purchasedProduct: String,
    override val change: List<Coin>
): Receipt

class ProcessedPurchase(
    override val sellerId: Int,
    override val buyerId: Int,
    override val productId: Int,
    override val productAmount: Int,
    override val costTotal: Double,
    override val amountPayed: Double
): Purchase 
