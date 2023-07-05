package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class AmenitiesScreen(
    @Json(name = "content")  val content: List<String>,
    @Json(name = "title")  val title: String
)