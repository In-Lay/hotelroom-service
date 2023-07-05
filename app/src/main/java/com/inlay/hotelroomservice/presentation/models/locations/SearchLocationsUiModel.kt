package com.inlay.hotelroomservice.presentation.models.locations

data class SearchLocationsUiModel(
    val title: String,
    val geoId: String?,
    val secondaryText: String,
    val image: SearchLocationsImageUiModel?
)
