package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class VeryGood(
    @Json(name = "count")  val count: String,
    @Json(name = "percentage")  val percentage: Int
)