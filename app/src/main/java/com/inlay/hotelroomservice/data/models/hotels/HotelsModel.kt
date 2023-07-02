package com.inlay.hotelroomservice.data.models.hotels

import com.squareup.moshi.Json

data class HotelsModel(
    @Json(name = "status") val status: Boolean,
    @Json(name = "message") val message: String,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "data") val generalData: GeneralData
)