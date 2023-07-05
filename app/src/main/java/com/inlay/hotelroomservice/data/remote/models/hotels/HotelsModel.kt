package com.inlay.hotelroomservice.data.remote.models.hotels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
data class HotelsModel(
    @Json(name = "status") val status: Boolean,
    @Json(name = "message") val message: String,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "data") val generalData: GeneralData
)