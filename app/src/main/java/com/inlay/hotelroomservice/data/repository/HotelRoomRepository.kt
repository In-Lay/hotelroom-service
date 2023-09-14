package com.inlay.hotelroomservice.data.repository

import com.inlay.hotelroomservice.presentation.models.AppResult
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.Flow

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
    ): AppResult<List<HotelsItemUiModel>, Int>

    suspend fun getHotelDetails(
        id: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): AppResult<HotelDetailsUiModel, Int>

    suspend fun getStaysRepo(isOnline: Boolean, isLogged: Boolean): Flow<List<HotelsItemUiModel?>>

    suspend fun addStaysRepo(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean)

    suspend fun removeStaysRepo(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean)


    suspend fun saveNightModeState(modeState: Int)

    suspend fun saveNotificationsState(state: Boolean)


    suspend fun getNightModeState(): Flow<Int>

    suspend fun getNotificationsState(): Flow<Boolean>
}