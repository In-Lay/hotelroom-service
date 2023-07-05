package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class GeoPoint(
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double
)