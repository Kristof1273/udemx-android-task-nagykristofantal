package com.nagykristofantal.udemxtask.data

data class CartItem(
    val iceCream: IceCream,
    val extras: List<ExtraItem>,
    val totalPrice: Double
)