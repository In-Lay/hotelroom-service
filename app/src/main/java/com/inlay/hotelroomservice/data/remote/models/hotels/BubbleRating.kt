package com.inlay.hotelroomservice.data.remote.models.hotels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BubbleRating(
    @Json(name = "count") val count: String,
    @Json(name = "rating") val rating: Double
)