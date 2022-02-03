@file:JvmName("GenericClasses")
package com.josavezaat.vmachine.common

data class ClientSession(
    val sessionId: String,
    override val id: Int,
    override var firstName: String,
    override var lastName: String,
    override var role: Role,
    override var userName: String
): User

class ApiResponse(val message: String) {
    var value: Any? = null
    constructor( message: String, value: Any): this(message) {
        this.value = value
    }
}

data class CoinDeposit(val coin: Coin)

data class RegisteredUser(
    override val id: Int,
    override var firstName: String,
    override var lastName: String,
    override var role: Role,
    override var userName: String
): User

data class PrivateUser(
    override val id: Int,
    override var firstName: String,
    override var lastName: String,
    override var role: Role,
    override var userName: String,
    override var password: String,
    override var deposit: Double = 0.00
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

open class NewPurchase(
    override val productId: Int,
    override val productAmount: Int,
    override val amountPayed: Double
): Purchase

class ProcessedPurchase(
    override var sellerId: Int,
    override var buyerId: Int,
    override val productId: Int,
    override val productAmount: Int,
    override var costTotal: Double,
    override val amountPayed: Double,
    override var productAmountAvailable: Int,
    override var buyersDepositAvailable: Double
): NewPurchase(productId, productAmount, amountPayed), PurchaseProcessing
