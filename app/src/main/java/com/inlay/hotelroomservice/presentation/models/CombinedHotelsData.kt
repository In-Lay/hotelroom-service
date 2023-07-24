package com.inlay.hotelroomservice.presentation.models

import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel

data class CombinedHotelsData<T>(
    val hotelsDataList: List<T>,
    val selectedDates: DatesModel,
    val selectedCurrency: String
)
