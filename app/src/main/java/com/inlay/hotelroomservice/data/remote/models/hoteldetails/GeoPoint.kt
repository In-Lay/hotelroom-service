package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.squareup.moshi.Json

data class GeoPoint(
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "longitude") val longitude: Double?
)