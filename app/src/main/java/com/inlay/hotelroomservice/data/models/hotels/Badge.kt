package com.inlay.hotelroomservice.data.models.hotels

import com.squareup.moshi.Json

data class Badge(
    @Json(name = "size") val size: String,
    @Json(name = "type") val type: String,
    @Json(name = "year") val year: String
)