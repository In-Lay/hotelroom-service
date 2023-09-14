package com.inlay.hotelroomservice.domain.usecase.details

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import com.inlay.hotelroomservice.presentation.models.AppResult
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel

class GetHotelDetailsImpl(private val repository: HotelRoomRepository) : GetHotelDetails {
    override suspend fun invoke(
        id: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): AppResult<HotelDetailsUiModel, Int> {
        return repository.getHotelDetails(
            id, checkInDate, checkOutDate, currencyCode
        )
    }
}