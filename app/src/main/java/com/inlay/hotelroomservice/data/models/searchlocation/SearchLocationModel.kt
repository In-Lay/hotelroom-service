package com.inlay.hotelroomservice.data.models.searchlocation

import com.squareup.moshi.Json

data class SearchLocationModel(
    @Json(name = "status") val status: Boolean,
    @Json(name = "message") val message: String,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "data") val data: List<Data>
)