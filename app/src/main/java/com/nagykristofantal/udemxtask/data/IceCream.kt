package com.nagykristofantal.udemxtask.data

import com.google.gson.annotations.SerializedName

enum class Status(val label: String) {
    @SerializedName("available")
    AVAILABLE("elérhető"),

    @SerializedName("melted")
    MELTED("kifogyott"),

    @SerializedName("unavailable")
    UNAVAILABLE("nem is volt")
}

data class IceCreamResponse(
    val iceCreams: List<IceCream>,
    val basePrice: Double
)

data class IceCream(
    val id: Long,
    val name: String,
    val status: Status,
    val imageUrl: String?
)