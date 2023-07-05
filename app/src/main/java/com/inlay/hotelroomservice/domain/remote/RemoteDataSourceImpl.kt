package com.inlay.hotelroomservice.domain.remote

import com.inlay.hotelroomservice.data.remote.apiservice.HotelRoomApiService
import com.inlay.hotelroomservice.data.remote.models.hoteldetails.HotelDetailsModel
import com.inlay.hotelroomservice.data.remote.models.hotels.HotelsModel
import com.inlay.hotelroomservice.data.remote.models.searchlocation.SearchLocationModel
import retrofit2.Response

class RemoteDataSourceImpl(private val hotelRoomApiService: HotelRoomApiService) :
    RemoteDataSource {
    override suspend fun getSearchLocationRepo(location: String): Response<SearchLocationModel> {
        return hotelRoomApiService.getSearchLocalData(location)
    }

    override suspend fun getHotelsRepo(
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): Response<HotelsModel> {
        return hotelRoomApiService.getHotelsData(geoId, checkInDate, checkOutDate, currencyCode)
    }

    override suspend fun getHotelDetailsRepo(
        id: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): Response<HotelDetailsModel> {
        return hotelRoomApiService.getHotelDetailsData(id, checkInDate, checkOutDate, currencyCode)
    }
}