package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.inlay.hotelroomservice.data.local.models.hoteldetails.RatingCounts
import com.inlay.hotelroomservice.data.local.models.hoteldetails.ReviewsContent
import com.squareup.moshi.Json

data class Reviews(
    @Json(name = "content") val reviewsContent: List<ReviewsContent>,
    @Json(name = "count") val count: Int,
    @Json(name = "ratingValue") val ratingValue: Double,
    @Json(name = "ratingCounts") val ratingCounts: RatingCounts
)