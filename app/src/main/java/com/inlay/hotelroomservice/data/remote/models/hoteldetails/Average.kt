package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class Average(
    @Json(name = "percentage") val percentage: Int,
    @Json(name = "count") val count: String
)