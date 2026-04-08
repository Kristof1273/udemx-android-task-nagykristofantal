package com.nagykristofantal.udemxtask.data

data class ExtraCategory(
    val type: String,
    val required: Boolean = false,
    val items: List<ExtraItem>
)

data class ExtraItem(
    val id: Long,
    val name: String,
    val price: Double
)