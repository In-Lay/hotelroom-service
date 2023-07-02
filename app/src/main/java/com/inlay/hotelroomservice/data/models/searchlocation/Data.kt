package com.inlay.hotelroomservice.data.models.searchlocation

import com.squareup.moshi.Json

data class Data(
    @Json(name = "title") val title: String,
    @Json(name = "geoId") val geoId: String,
    @Json(name = "documentId") val documentId: String?,
    @Json(name = "trackingItems") val trackingItems: String?,
    @Json(name = "secondaryText") val secondaryText: String,
    @Json(name = "image") val image: Image?
)