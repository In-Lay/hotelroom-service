package com.inlay.hotelroomservice.domain.usecase.hotels

import com.inlay.hotelroomservice.presentation.models.AppResult
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel

interface GetHotelsRepo {
    suspend operator fun invoke(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String = "USD"
    ): AppResult<List<HotelsItemUiModel>, Int>
}