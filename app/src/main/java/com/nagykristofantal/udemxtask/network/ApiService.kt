package com.nagykristofantal.udemxtask.network

import com.nagykristofantal.udemxtask.data.IceCreamResponse
import com.nagykristofantal.udemxtask.data.ExtraCategory
import com.nagykristofantal.udemxtask.data.OrderPayload
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Headers

interface ApiService {
    @GET("icecreams.json")
    suspend fun getIceCreams(): IceCreamResponse

    @GET("extras.json")
    suspend fun getExtras(): List<ExtraCategory>

    @POST("https://httpbin.org/post")
    suspend fun postOrder(@Body order: List<OrderPayload>): retrofit2.Response<Unit>

    @Headers("User-Agent: Android-IceCream-App")
    @POST("https://httpbin.org/post")
    suspend fun placeOrder(@Body order: List<OrderPayload>): Response<Any>
}