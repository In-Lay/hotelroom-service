package com.inlay.hotelroomservice.data.remote.models.searchlocation

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "typename") val typename: String,
    @Json(name = "maxHeight") val maxHeight: Int,
    @Json(name = "maxWidth") val maxWidth: Int,
    @Json(name = "urlTemplate") val urlTemplate: String
)