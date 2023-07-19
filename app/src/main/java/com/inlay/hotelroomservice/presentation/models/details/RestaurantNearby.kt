package com.inlay.hotelroomservice.presentation.models.details

data class RestaurantNearby(
    val title: String,
    val primaryInfo: String,
    val distance: String,
    val photoUrlTemplate: String,
    val rating: Double,
    val numberReviews: String
)
