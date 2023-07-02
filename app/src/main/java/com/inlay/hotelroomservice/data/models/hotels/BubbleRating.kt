package com.inlay.hotelroomservice.data.models.hotels

import com.squareup.moshi.Json

data class BubbleRating(
    @Json(name = "count") val count: String,
    @Json(name = "rating") val rating: Double
)