package com.example.ecoreport.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://6ae26633bbb541292299ece2.mockapi.io/ecoreport/"

    val api:
            ReporteApiService by lazy {

        Retrofit.Builder()
            .baseUrl(
                BASE_URL
            )
            .addConverterFactory(
                GsonConverterFactory
                    .create()
            )
            .build()
            .create(
                ReporteApiService::class.java
            )
    }
}