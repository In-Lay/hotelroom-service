package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class CardPhoto(
    @Json(name = "maxHeight") val maxHeight: Int,
    @Json(name = "maxWidth") val maxWidth: Int,
    @Json(name = "urlTemplate") val urlTemplate: String
)