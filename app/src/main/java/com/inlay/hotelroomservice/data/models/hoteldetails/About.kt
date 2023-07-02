package com.inlay.hotelroomservice.data.models.hoteldetails

data class About(
    val content: List<Content>,
    val tags: List<String>,
    val title: String
)