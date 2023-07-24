package com.inlay.hotelroomservice.presentation.models.details

data class HotelDetailsUiModel(
    val title: String,
    val rating: String,
    val numberReviews: String,
    val rankingDetails: String,
    val displayPrice: String,
    val providerName: String,
    val photos: List<String>,
    val aboutTitle: String,
    val aboutLinks: List<String>,
    val aboutAmenities: List<String>,
    val address: String,
    val restaurantsNearby: List<NearbyPlace.RestaurantNearby>,
    val attractionsNearby: List<NearbyPlace.AttractionNearby>,
    val latitude: Double,
    val longitude: Double,
)
