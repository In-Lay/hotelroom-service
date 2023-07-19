package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.inlay.hotelroomservice.data.local.models.hoteldetails.CardPhoto
import com.squareup.moshi.Json

data class AttractionsNearbyContent(
    @Json(name = "bubbleRating") val bubbleRating: BubbleRating,
    @Json(name = "cardPhoto") val cardPhoto: CardPhoto,
    @Json(name = "distance") val distance: String,
    @Json(name = "primaryInfo") val primaryInfo: String,
    @Json(name = "title") val title: String
)