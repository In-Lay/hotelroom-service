package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class RestaurantsNearby(
    @Json(name = "content") val restaurantsNearbyContent: List<RestaurantsNearbyContent>,
    @Json(name = "sectionTitle") val sectionTitle: String
)