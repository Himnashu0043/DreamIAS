package com.example.dream_ias.network

import com.example.dream_ias.BuildConfig
import com.example.dream_ias.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClient {

    fun get(): Retrofit {
        val logging = HttpLoggingInterceptor() // logs HTTP request and response data.

        logging.setLevel(HttpLoggingInterceptor.Level.BODY) // set your desired log level

        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    fun getApi() = get().create(Api::class.java)

}