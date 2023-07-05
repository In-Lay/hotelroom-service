package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class TopAnswer(
    @Json(name = "memberProfile") val topAnswerMemberProfile: TopAnswerMemberProfile,
    @Json(name = "text") val text: String,
    @Json(name = "thumbsUpCount") val thumbsUpCount: Int,
    @Json(name = "writtenDate") val writtenDate: String
)