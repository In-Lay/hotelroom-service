package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.squareup.moshi.Json

data class QA(
    @Json(name = "content") val qaContent: List<QAContent>
)