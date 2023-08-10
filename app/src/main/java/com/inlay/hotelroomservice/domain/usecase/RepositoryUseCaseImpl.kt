package com.inlay.hotelroomservice.domain.usecase

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.Flow

class RepositoryUseCaseImpl(private val hotelRoomRepository: HotelRoomRepository) :
    RepositoryUseCase {
    override suspend fun getSearchLocationRepo(location: String): List<SearchLocationsUiModel> {
        return hotelRoomRepository.getSearchLocationRepo(location)
    }

    override suspend fun getHotelsRepo(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): List<HotelsItemUiModel> {
        return hotelRoomRepository.getHotelRepo(
            isOnline, geoId, checkInDate, checkOutDate, currencyCode
        )
    }

    override suspend fun getHotelDetailsRepo(
        id: String, checkInDate: String, checkOutDate: String, currencyCode: String
    ): HotelDetailsUiModel {
        return hotelRoomRepository.getHotelDetails(
            id, checkInDate, checkOutDate, currencyCode
        )
    }

    override suspend fun getStaysRepo(
        isOnline: Boolean,
        isLogged: Boolean
    ): Flow<List<HotelsItemUiModel?>> {
        return hotelRoomRepository.getStaysRepo(isOnline, isLogged)
    }

    override suspend fun addStayRepo(
        hotelsItem: HotelsItemUiModel,
        isOnline: Boolean,
        isLogged: Boolean
    ) {
        hotelRoomRepository.addStaysRepo(hotelsItem, isOnline, isLogged)
    }

    override suspend fun removeStayRepo(
        hotelsItem: HotelsItemUiModel,
        isOnline: Boolean,
        isLogged: Boolean
    ) {
        hotelRoomRepository.removeStaysRepo(hotelsItem, isOnline, isLogged)
    }


    override suspend fun saveLanguage(langCode: String) {
        hotelRoomRepository.saveLanguage(langCode)
    }

    override suspend fun saveNightModeState(modeState: Int) {
        hotelRoomRepository.saveNightModeState(modeState)
    }

    override suspend fun saveNotificationsState(state: Boolean) {
        hotelRoomRepository.saveNotificationsState(state)
    }

    override suspend fun getLanguage(): Flow<String> {
        return hotelRoomRepository.getLanguage()
    }

    override suspend fun getNightModeState(): Flow<Int> {
        return hotelRoomRepository.getNightModeState()
    }

    override suspend fun getNotificationsState(): Flow<Boolean> {
        return hotelRoomRepository.getNotificationsState()
    }
}