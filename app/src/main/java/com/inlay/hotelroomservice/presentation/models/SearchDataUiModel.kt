package com.inlay.hotelroomservice.presentation.models

data class SearchDataUiModel(
    val geoId: String?,
    val checkInDate: String,
    val checkOutDate: String,
    val currencyCode: String
)
