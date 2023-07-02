package com.inlay.hotelroomservice.data.models.hotels

import com.squareup.moshi.Json

data class GeneralData(
    @Json(name = "sortDisclaimer") val sortDisclaimer: String,
    @Json(name = "data") val dataList: List<Data>
)