package com.inlay.hotelroomservice.presentation.models.details

data class HotelDetailsUiModel(
    val title: String,
    val rating: Double,
    val numberReviews: Int,
    val rankingDetails: String,
    val displayPrice: String,
    val providerName: String,
    val photos: List<String>,
    val aboutTitle: String,
    val aboutLink: String,
    val aboutAmenities: List<String>,
    val address: String,
    val restaurantsNearby: List<RestaurantNearby>,
    val attractionsNearby: List<AttractionNearby>,
    val latitude: Double,
    val longitude: Double,
)
