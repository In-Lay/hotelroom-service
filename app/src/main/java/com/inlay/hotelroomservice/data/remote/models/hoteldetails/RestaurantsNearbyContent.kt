package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.squareup.moshi.Json

data class RestaurantsNearbyContent(
    @Json(name = "bubbleRating") val bubbleRating: BubbleRating,
    @Json(name = "cardPhoto") val restaurantsNearbyCardPhoto: RestaurantsNearbyCardPhoto,
    @Json(name = "distance") val distance: String,
    @Json(name = "primaryInfo") val primaryInfo: String,
    @Json(name = "title") val title: String
)