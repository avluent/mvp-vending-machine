@file:JvmName("Interfaces")
package com.josavezaat.vmachine.common

interface VendingMachine {

    // purchases
    fun buyProduct(purchase: Purchase): Receipt
    val purchases: MutableList<Purchase>

    // users
    fun listUsers(): List<User>
    fun createUser(user: User): User
    fun updateUser(userId: Int, data: User): User
    fun removeUser(userId: Int): Int

    // deposits
    fun depositCoin(userId: Int, coin: Coin): Double
    fun settleSellerCompensation(sellerId: Int, amount: Double): Double
    fun resetDeposit(userId: Int): Int

    // products
    fun listProducts(): List<Product>
    fun createProduct(product: Product): Product
    fun updateProduct(productId: Int, data: Product): Product
    fun removeProduct(productId: Int): Int

}

interface User {

    val id: Int
    var firstName: String
    var lastName: String
    var role: Role

}

interface PrivateUserData {

    var username: String
    var password: String
    var deposit: Double

}

interface Product {

    val id: Int
    var name: String
    var amountAvailable: Int
    var cost: Double

}

interface PrivateProductData {

    var sellerId: Int

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