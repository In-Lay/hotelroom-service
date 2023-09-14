package com.inlay.hotelroomservice.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

fun makeNetworkService(moshi: Moshi, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://tripadvisor16.p.rapidapi.com/api/v1/hotels/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
}

fun makeMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(CustomMoshiAdapter())
        .build()
}

fun makeHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    return OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .addInterceptor(Interceptor {
            try {
                val response = it.proceed(it.request())
                response
            } catch (e: Exception) {
                val builder = Response.Builder()
                    .message(e.message!!)
                    .code(493)
                    .protocol(Protocol.HTTP_1_1)
                    .request(it.request())

                builder.build()
            }
        })
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}