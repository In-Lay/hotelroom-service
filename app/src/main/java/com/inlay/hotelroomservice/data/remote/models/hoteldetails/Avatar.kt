package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.squareup.moshi.Json

data class Avatar(
    @Json(name = "maxHeight") val maxHeight: Int?,
    @Json(name = "maxWidth") val maxWidth: Int?,
    @Json(name = "urlTemplate") val urlTemplate: String?
)