package com.inlay.hotelroomservice.data.remote.models.hotels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeneralData(
//    @Json(name = "sortDisclaimer") val sortDisclaimer: String?,
    @Json(name = "data") val dataList: List<Data>
)