package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.squareup.moshi.Json

data class About(
    @Json(name = "title") val title: String?,
    @Json(name = "content") val aboutContentGeneral: List<AboutContentGeneral>,
    @Json(name = "tags") val tags: List<String>,
)