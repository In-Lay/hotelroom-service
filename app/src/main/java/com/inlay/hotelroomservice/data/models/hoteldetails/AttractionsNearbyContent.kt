package com.inlay.hotelroomservice.data.models.hoteldetails

import com.squareup.moshi.Json

data class AttractionsNearbyContent(
    @Json(name = "bubbleRating") val bubbleRating: BubbleRating,
    @Json(name = "cardPhoto") val cardPhoto: CardPhoto,
    @Json(name = "distance") val distance: String,
    @Json(name = "primaryInfo") val primaryInfo: String,
    @Json(name = "title") val title: String
)