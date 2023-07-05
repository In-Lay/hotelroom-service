package com.inlay.hotelroomservice.data.remote.apiservice

import com.inlay.hotelroomservice.data.remote.models.hoteldetails.HotelDetailsModel
import com.inlay.hotelroomservice.data.remote.models.hotels.HotelsModel
import com.inlay.hotelroomservice.data.remote.models.searchlocation.SearchLocationModel
import retrofit2.Response

interface HotelRoomApiService {
    suspend fun getSearchLocalData(location: String): Response<SearchLocationModel>

    suspend fun getHotelsData(
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String = "USD"
    ): Response<HotelsModel>

    suspend fun getHotelDetailsData(
        id: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): Response<HotelDetailsModel>
}