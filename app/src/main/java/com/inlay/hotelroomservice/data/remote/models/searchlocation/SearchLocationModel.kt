package com.inlay.hotelroomservice.data.remote.models.searchlocation

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchLocationModel(
    @Json(name = "status") val status: Boolean?,
    @Json(name = "message") val message: String?,
    @Json(name = "timestamp") val timestamp: Long?,
    @Json(name = "data") val data: List<Data>
)