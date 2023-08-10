package com.inlay.hotelroomservice.domain.usecase

import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.Flow

interface RepositoryUseCase {
    suspend fun getSearchLocationRepo(location: String): List<SearchLocationsUiModel>

    suspend fun getHotelsRepo(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String = "USD"
    ): List<HotelsItemUiModel>

    suspend fun getHotelDetailsRepo(
        id: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): HotelDetailsUiModel

    suspend fun getStaysRepo(isOnline: Boolean, isLogged: Boolean): Flow<List<HotelsItemUiModel?>>

    suspend fun addStayRepo(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean)

    suspend fun removeStayRepo(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean)


    suspend fun saveLanguage(langCode: String)

    suspend fun saveNightModeState(modeState: Int)

    suspend fun saveNotificationsState(state: Boolean)


    suspend fun getLanguage(): Flow<String>

    suspend fun getNightModeState(): Flow<Int>

    suspend fun getNotificationsState(): Flow<Boolean>
}