package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class MemberProfile(
    @Json(name = "profileImage") val profileImage: ProfileImage?
)