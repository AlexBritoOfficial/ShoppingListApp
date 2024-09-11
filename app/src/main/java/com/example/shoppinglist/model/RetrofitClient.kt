package com.example.shoppinglist.model

import com.example.shoppinglist.network.GeoCodingApiService
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL : String =  "https://maps.googleapis.com/"

    fun create(): GeoCodingApiService {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

        return  retrofit.create(GeoCodingApiService::class.java)
    }
}