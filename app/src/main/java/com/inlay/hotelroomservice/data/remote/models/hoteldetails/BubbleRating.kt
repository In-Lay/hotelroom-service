package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.squareup.moshi.Json

data class BubbleRating(
    @Json(name = "numberReviews") val numberReviews: String?,
    @Json(name = "rating") val rating: Double?
)