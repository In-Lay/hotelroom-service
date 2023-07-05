package com.inlay.hotelroomservice.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitObject {
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client: OkHttpClient =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    val makeRetrofit: Retrofit =
        Retrofit.Builder().baseUrl("https://tripadvisor16.p.rapidapi.com/api/v1/hotels/")
            .addConverterFactory(
                MoshiConverterFactory.create(moshi)
            ).client(client).build()
}