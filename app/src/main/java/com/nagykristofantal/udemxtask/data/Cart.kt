package com.nagykristofantal.udemxtask.data

data class CartItem(
    val iceCream: IceCream,
    val selectedExtras: List<ExtraItem>
)

data class OrderPayload(
    val id: Long,
    val extra: List<Long>
)