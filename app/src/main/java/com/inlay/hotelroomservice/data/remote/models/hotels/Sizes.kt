package com.inlay.hotelroomservice.data.remote.models.hotels

import com.squareup.moshi.Json

data class Sizes(
    @Json(name = "typename") val typename: String,
    @Json(name = "maxHeight") val maxHeight: Int,
    @Json(name = "maxWidth") val maxWidth: Int,
    @Json(name = "urlTemplate") val urlTemplate: String
)