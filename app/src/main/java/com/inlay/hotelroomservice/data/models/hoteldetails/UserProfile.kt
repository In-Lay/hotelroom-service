package com.inlay.hotelroomservice.data.models.hoteldetails

import com.squareup.moshi.Json

data class UserProfile(
    @Json(name = "deprecatedContributionCount") val deprecatedContributionCount: String,
    @Json(name = "avatar") val avatar: Avatar
)