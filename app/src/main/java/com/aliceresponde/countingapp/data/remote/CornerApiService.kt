package com.aliceresponde.countingapp.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.http.hasBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CornerApiService {
    companion object {
        private const val BASE_URL = "https://d112483dea9b.ngrok.io/"
            //"http://192.168.18.56:3000"
            //"http://10.0.2.2:3000/"   virtual device

        operator fun invoke(interceptor: Interceptor): CornerApiService {
            val logInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(logInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CornerApiService::class.java)
        }
    }

    @Headers("Content-Type: application/json")
    @GET("api/v1/counters")
    suspend fun getCounters(): List<CounterResponse>

    @Headers("Content-Type: application/json")
    @POST("api/v1/counter")
    suspend fun createCounter(@Body request: CreateCounterBody): List<CounterResponse>

    @Headers("Content-Type: application/json")
    @POST("api/v1/counter/inc")
    suspend fun incrementCounter(@Body request: ModifyCounterBody): List<CounterResponse>

    @Headers("Content-Type: application/json")
    @POST("api/v1/counter/dec")
    suspend fun decrementCounter(@Body request: ModifyCounterBody): List<CounterResponse>

//    @Headers("Content-Type: application/json")
//    @DELETE("api/v1/counter")
    @HTTP(method = "DELETE", path = "api/v1/counter", hasBody = true)
    suspend fun deleteCounter(@Body request: ModifyCounterBody): List<CounterResponse>
}