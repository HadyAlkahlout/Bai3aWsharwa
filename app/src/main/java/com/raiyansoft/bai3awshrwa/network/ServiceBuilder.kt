package com.raiyansoft.bai3awshrwa.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ServiceBuilder {

    companion object {

        private val baseURL = "https://bai3awshrwa.com/api/"
        var apis: Api? = null


        init {
            val client = OkHttpClient.Builder()
                .build()
            apis = getRetrofitInstance(client).create(Api::class.java)
        }

        fun getRetrofitInstance(client:OkHttpClient): Retrofit =
             Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

    }

}