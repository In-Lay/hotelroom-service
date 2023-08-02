package com.inlay.hotelroomservice.data.repository

import com.inlay.hotelroomservice.data.local.models.HotelsItemStaysEntity
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel

interface HotelRoomRepository {
    suspend fun getSearchLocationRepo(
        location: String
    ): List<SearchLocationsUiModel>

    suspend fun getHotelRepo(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String = "USD"
    ): List<HotelsItemUiModel>

    suspend fun getHotelDetails(
        id: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): HotelDetailsUiModel

    suspend fun getStaysRepo(isOnline: Boolean, isLogged: Boolean): List<HotelsItemUiModel>

    suspend fun addStaysRepo(hotelsItem: HotelsItemUiModel)

    suspend fun removeStaysRepo(hotelsItem: HotelsItemUiModel)
}