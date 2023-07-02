package com.inlay.hotelroomservice.data.models.hotels

import com.squareup.moshi.Json

data class CardPhoto(
    @Json(name = "typename") val typename: String,
    @Json(name = "sizes") val sizes: Sizes
)