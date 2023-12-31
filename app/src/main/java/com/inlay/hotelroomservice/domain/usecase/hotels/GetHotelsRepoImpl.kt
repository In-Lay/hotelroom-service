package com.inlay.hotelroomservice.domain.usecase.hotels

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import com.inlay.hotelroomservice.presentation.models.AppResult
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel

class GetHotelsRepoImpl(private val hotelRoomRepository: HotelRoomRepository) : GetHotelsRepo {
    override suspend fun invoke(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): AppResult<List<HotelsItemUiModel>, Int> {
        return hotelRoomRepository.getHotelRepo(
            isOnline, geoId, checkInDate, checkOutDate, currencyCode
        )
    }
}