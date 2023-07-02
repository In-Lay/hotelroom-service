package com.inlay.hotelroomservice.data.models.searchlocation

import com.squareup.moshi.Json

data class Image(
    @Json(name = "typename") val typename: String,
    @Json(name = "maxHeight") val maxHeight: Int,
    @Json(name = "maxWidth")  val maxWidth: Int,
    @Json(name = "urlTemplate") val urlTemplate: String
)