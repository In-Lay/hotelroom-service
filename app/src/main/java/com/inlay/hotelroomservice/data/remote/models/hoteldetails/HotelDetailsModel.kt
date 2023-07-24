package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.squareup.moshi.Json

data class HotelDetailsModel(
    @Json(name = "status") val status: Boolean?,
    @Json(name = "message") val message: String?,
    @Json(name = "timestamp") val timestamp: Long?,
    @Json(name = "data") val data: Data?
)