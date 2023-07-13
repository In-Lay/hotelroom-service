package com.inlay.hotelroomservice.presentation.models.details

data class HotelDetailsUiModel(
    val title: String,
    val rating: Double,
    val numberReviews: Int,
    val rankingDetails: String,
    val photos: List<String>,
    val about: String,
    val geoPoint: GeoPointUiModel,
)
