package com.inlay.hotelroomservice.data.remote.models.hotels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Badge(
    @Json(name = "size") val size: String?,
    @Json(name = "type") val type: String?,
    @Json(name = "year") val year: String?
)