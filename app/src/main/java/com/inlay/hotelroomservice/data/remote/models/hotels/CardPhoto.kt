package com.inlay.hotelroomservice.data.remote.models.hotels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardPhoto(
    @Json(name = "typename") val typename: String?,
    @Json(name = "sizes") val sizes: Sizes
)