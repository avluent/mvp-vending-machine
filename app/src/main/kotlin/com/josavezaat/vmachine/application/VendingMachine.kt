@file:JvmName("VendingMachine")
package com.josavezaat.vmachine.application

import com.josavezaat.vmachine.common.*

object CandyBarMachine: VendingMachine {

    override val purchases = mutableListOf<Purchase>()

    override fun buyProduct(purchase: Purchase): CustomerReceipt {
        // mock function return data
        return CustomerReceipt(
            1.20,
            "Snix",
            listOf()
        )
    }

    // User functions
    override fun listUsers(): List<RegisteredUser> { 
        return listOf(RegisteredUser(3, "John", "Macky", Role.BUYER))
    }
    override fun createUser(user: User): RegisteredUser { 
        return RegisteredUser(3, "John", "Macky", Role.BUYER)
    }
    override fun updateUser(userId: Int, data: User): PrivateUser {
        return PrivateUser(
            3, "John", "Macky", Role.BUYER, "j.m@me.com", "son343jkdf", 3.30
        )
     }
    override fun removeUser(userId: Int): Int { 
        return 0
    }

    // Deposit Functions
    override fun depositCoin(userId: Int, coin: Coin): Double {
        return 1.00
    }
    override fun settleSellerCompensation(sellerId: Int, amount: Double): Double {
        return 1.00
    }
    override fun resetDeposit(userId: Int): Int {
        return 0
    }

    // Product functions
    override fun listProducts(): List<PresentableProduct> {
        return listOf(PresentableProduct(2, "Jix", 5, 2.25))
    }
    override fun createProduct(product: Product): PresentableProduct {
        return PresentableProduct(2, "Jix", 5, 2.25)
    }
    override fun updateProduct(productId: Int, data: Product): FullProduct {
        return FullProduct(2, "Jive", 19, 0.90, 5)
    }
    override fun removeProduct(productId: Int): Int {
        return 0
    }

}