package com.inlay.hotelroomservice.data.models.hoteldetails

data class TopAnswer(
    val memberProfile: MemberProfileX,
    val text: String,
    val thumbsUpCount: Int,
    val writtenDate: String
)