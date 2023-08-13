package com.inlay.hotelroomservice.domain.usecase.details

import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel

interface GetHotelDetails {
    suspend operator fun invoke(
        id: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): HotelDetailsUiModel
}