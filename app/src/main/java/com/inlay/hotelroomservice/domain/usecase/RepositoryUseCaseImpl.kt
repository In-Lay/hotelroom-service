package com.inlay.hotelroomservice.domain.usecase

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel

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
            isOnline,
            geoId,
            checkInDate,
            checkOutDate,
            currencyCode
        )
    }

    override suspend fun getHotelDetailsRepo(
        id: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): HotelDetailsUiModel {
        return hotelRoomRepository.getHotelDetails(
            id,
            checkInDate,
            checkOutDate,
            currencyCode
        )
    }

    override suspend fun getStaysRepo(isOnline: Boolean): List<HotelsItemUiModel> {
        return hotelRoomRepository.getStaysRepo(isOnline)
    }

    override suspend fun addStayRepo(hotelsItem: HotelsItemUiModel) {
        hotelRoomRepository.addStaysRepo(hotelsItem)
    }

    override suspend fun removeStayRepo(hotelsItem: HotelsItemUiModel) {
        hotelRoomRepository.removeStaysRepo(hotelsItem)
    }
}