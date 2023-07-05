package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class AboutContentGeneral(
    @Json(name = "title") val title: String,
    @Json(name = "content") val contentAbout: List<ContentAbout>,
)