@file:JvmName("Interfaces")
package com.josavezaat.vmachine.common

interface VendingMachine {

    // purchases
    fun buyProduct(_purchase: NewPurchase): Receipt?
    val purchases: MutableList<ProcessedPurchase>

    // users
    fun listUsers(): List<User>
    fun createUser(user: PrivateUser): User?
    fun updateUser(userId: Int, data: Map<String, Any>): PrivateUser?
    fun removeUser(userId: Int): Int

    // deposits
    fun depositCoin(userId: Int, coin: Coin): Double
    fun settleSellerCompensation(sellerId: Int, amount: Double): Double
    fun resetDeposit(userId: Int): Int

    // products
    fun listProducts(): List<Product>
    fun createProduct(product: FullProduct): PresentableProduct?
    fun updateProduct(productId: Int, data: Map<String, Any>): FullProduct?
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

    val productId: Int
    val productAmount: Int
    val amountPayed: Double

}

interface PurchaseProcessing {

    var sellerId: Int
    var buyerId: Int
    var costTotal: Double
    var productAmountAvailable: Int
    var buyersDepositAvailable: Double

}

interface Receipt {

    val totalSpent: Double
    val purchasedProduct: String
    val change: List<Coin>

}