package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.squareup.moshi.Json

data class TopAnswerMemberProfile(
    @Json(name = "contributionCounts") val contributionCounts: Int?,
    @Json(name = "deprecatedContributionCount") val deprecatedContributionCount: String?,
    @Json(name = "displayName") val displayName: String?,
    @Json(name = "hometown") val hometown: String?,
    @Json(name = "profileImage") val topAnswerMemberProfile: TopAnswerMemberProfileImage?
)