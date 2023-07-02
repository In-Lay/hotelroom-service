package com.inlay.hotelroomservice.data.models.hoteldetails

data class RatingCounts(
    val average: Average,
    val excellent: Excellent,
    val poor: Poor,
    val terrible: Terrible,
    val veryGood: VeryGood
)