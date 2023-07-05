package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class ReviewsContent(
    @Json(name = "title") val title: String,
    @Json(name = "text") val text: String,
    @Json(name = "bubbleRatingText") val bubbleRatingText: String,
    @Json(name = "publishedDate") val publishedDate: String,
    @Json(name = "userProfile") val userProfile: UserProfile,
    @Json(name = "photos") val photos: List<Any?>
)