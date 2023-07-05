package com.inlay.hotelroomservice.data.remote.api

import com.inlay.hotelroomservice.data.remote.models.hoteldetails.HotelDetailsModel
import com.inlay.hotelroomservice.data.remote.models.hotels.HotelsModel
import com.inlay.hotelroomservice.data.remote.models.searchlocation.SearchLocationModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface HotelRoomApi {
    @Headers(
        "X-RapidAPI-Key: ${com.inlay.hotelroomservice.BuildConfig.HOTELSROOM_SERVICE_API_KEY}",
        "X-RapidAPI-Host: tripadvisor16.p.rapidapi.com"
    )
    @GET("searchLocation?")
    suspend fun searchHotelsLocation(@Query("query") location: String): Response<SearchLocationModel>

    @Headers(
        "X-RapidAPI-Key: ${com.inlay.hotelroomservice.BuildConfig.HOTELSROOM_SERVICE_API_KEY}",
        "X-RapidAPI-Host: tripadvisor16.p.rapidapi.com"
    )
    @GET("searchHotels?")
    suspend fun getHotelsData(
        @Query("geoId") geoId: String,
        @Query("checkIn") checkInDate: String,
        @Query("checkOut") checkOutDate: String,
        @Query("currencyCode") currencyCode: String = "USD"
    ): Response<HotelsModel>

    @Headers(
        "X-RapidAPI-Key: ${com.inlay.hotelroomservice.BuildConfig.HOTELSROOM_SERVICE_API_KEY}",
        "X-RapidAPI-Host: tripadvisor16.p.rapidapi.com"
    )
    @GET("getHotelDetails?")
    suspend fun getHotelDetails(
        @Query("id")
        id: String,
        @Query("checkIn")
        checkInDate: String,
        @Query("checkOut")
        checkOutDate: String,
        @Query("currency")
        currencyCode: String
    ): Response<HotelDetailsModel>
}