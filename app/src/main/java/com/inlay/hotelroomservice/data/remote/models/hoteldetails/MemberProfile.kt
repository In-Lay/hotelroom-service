package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.squareup.moshi.Json

data class MemberProfile(
    @Json(name = "profileImage") val profileImage: ProfileImage?
)