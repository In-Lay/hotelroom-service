package com.inlay.hotelroomservice.data.models.searchlocation

import com.squareup.moshi.Json

data class SearchLocationModel(
    @Json(name = "data") val data: List<Data>,
    @Json(name = "message") val message: String,
    @Json(name = "status") val status: Boolean,
    @Json(name = "timestamp") val timestamp: Long
)