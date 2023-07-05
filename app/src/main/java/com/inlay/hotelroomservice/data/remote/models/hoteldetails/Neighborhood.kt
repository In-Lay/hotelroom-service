package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class Neighborhood(
    @Json(name = "name") val name: String,
    @Json(name = "text") val text: Any
)