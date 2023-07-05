package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class ContentAbout(
    @Json(name = "content") val content: String,
    @Json(name = "title") val title: String
)