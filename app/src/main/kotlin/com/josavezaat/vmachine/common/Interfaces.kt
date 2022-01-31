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
    buyProduct()
    purchases: MutableList<Purchase>

    // users
    listUsers()
    createUser(user: User)
    updateUser(userId: String, data: User)
    removeUser(userId: String)

    // deposits
    depositCoin(userId: String, coin: Coin)
    settleSellerCompensation(sellerId: String, amount: Double)
    resetDeposit(userId: String)

    // products
    listProducts()
    createProduct(product: Product)
    updateProduct(productId: String, data: Product)
    removeProduct(productId: String)

}

interface User {

    val id: String
    var username: String
    var password: String
    var deposit: Double
    var role: Role

}

interface Product {

    val id: String
    val sellerId: String
    var name: String
    var amountAvailable: Int
    var cost: Double

}

interface Purchase {

    val sellerId: String
    val buyerId: String
    val productId: String
    val productAmount: String
    val costTotal: String

}