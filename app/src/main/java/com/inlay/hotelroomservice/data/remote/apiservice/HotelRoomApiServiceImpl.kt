package com.inlay.hotelroomservice.data.remote.apiservice

import com.inlay.hotelroomservice.data.local.api.HotelRoomApi
import com.inlay.hotelroomservice.data.remote.models.hoteldetails.HotelDetailsModel
import com.inlay.hotelroomservice.data.remote.models.hotels.HotelsModel
import com.inlay.hotelroomservice.data.remote.models.searchlocation.SearchLocationModel
import retrofit2.Response

class HotelRoomApiServiceImpl(private val hotelRoomApi: HotelRoomApi) : HotelRoomApiService {
    override suspend fun getSearchLocalData(location: String): Response<SearchLocationModel> {
        return hotelRoomApi.searchHotelsLocation(location)
    }

    override suspend fun getHotelsData(
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): Response<HotelsModel> {
        return hotelRoomApi.getHotelsData(geoId, checkInDate, checkOutDate, currencyCode)
    }

    override suspend fun getHotelDetailsData(
        id: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): Response<HotelDetailsModel> {
        return hotelRoomApi.getHotelDetails(id, checkInDate, checkOutDate, currencyCode)
    }
}