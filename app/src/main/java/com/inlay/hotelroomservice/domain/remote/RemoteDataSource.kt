package com.inlay.hotelroomservice.domain.remote

import com.inlay.hotelroomservice.data.remote.models.hoteldetails.HotelDetailsModel
import com.inlay.hotelroomservice.data.remote.models.hotels.HotelsModel
import com.inlay.hotelroomservice.data.remote.models.searchlocation.SearchLocationModel
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getSearchLocationRepo(location: String): Response<SearchLocationModel>

    suspend fun getHotelsRepo(
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): Response<HotelsModel>

    suspend fun getHotelDetailsRepo(
        id: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String = "USD"
    ): Response<HotelDetailsModel>
}