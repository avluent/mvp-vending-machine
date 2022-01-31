@file:JvmName("Interfaces")
package com.josavezaat.vmachine.common

enum class Role {
    BUYER, SELLER
}

enum class Coin(value: Double) {
    FIVECENTS(0.05),
    TENCENTS(0.10),
    TWENTYCENTS(0.20),
    FIFTYCENTS(0.50),
    HUNDREDCENTS(1.00)
}

interface VendingMachine {

    // purchases
    fun buyProduct(purchase: Purchase): Receipt
    val purchases: MutableList<Purchase>

    // users
    fun listUsers()
    fun createUser(user: User)
    fun updateUser(userId: String, data: User)
    fun removeUser(userId: String)

    // deposits
    fun depositCoin(userId: String, coin: Coin)
    fun settleSellerCompensation(sellerId: String, amount: Double)
    fun resetDeposit(userId: String)

    // products
    fun listProducts()
    fun createProduct(product: Product)
    fun updateProduct(productId: String, data: Product)
    fun removeProduct(productId: String)

}

interface User {

    val id: String
    var firstName: String
    var lastName: String
    var role: Role

}

interface Product {

    val id: String
    val sellerId: String?
    var name: String
    var amountAvailable: Int
    var cost: Double

}

interface Purchase {

    val sellerId: String?
    val buyerId: String?
    val productId: String
    val productAmount: Int
    val costTotal: Double?

}

interface Receipt {

    val totalSpent: Double
    val purchasedProduct: String
    val change: List<Coin>

}

class ApiResponse(
    val message: String
) {

    lateinit var value: Any
    constructor( message: String, value: Any): this(message) {
        this.value = value
    }
}
