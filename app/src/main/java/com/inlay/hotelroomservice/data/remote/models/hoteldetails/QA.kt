package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class QA(
    @Json(name = "content") val qaContent: List<QAContent>
)