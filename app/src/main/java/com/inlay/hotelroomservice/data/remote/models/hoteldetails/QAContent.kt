package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.squareup.moshi.Json

data class QAContent(
    @Json(name = "memberProfile") val memberProfile: MemberProfile?,
    @Json(name = "title") val title: String?,
    @Json(name = "topAnswer")  val topAnswer: TopAnswer?,
    @Json(name = "writtenDate") val writtenDate: String?
)