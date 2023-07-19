package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.inlay.hotelroomservice.data.remote.models.hoteldetails.Poor
import com.squareup.moshi.Json

data class RatingCounts(
    @Json(name = "average") val average: Average,
    @Json(name = "excellent") val excellent: Excellent,
    @Json(name = "poor") val poor: Poor,
    @Json(name = "terrible") val terrible: Terrible,
    @Json(name = "veryGood") val veryGood: VeryGood
)