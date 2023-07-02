package com.inlay.hotelroomservice.data.models.hoteldetails

import com.squareup.moshi.Json

data class MemberProfile(
    @Json(name = "profileImage") val profileImage: ProfileImage?
)