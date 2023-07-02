package com.inlay.hotelroomservice.data.models.hoteldetails

data class Location(
    val address: String,
    val gettingThere: GettingThere,
    val neighborhood: Neighborhood,
    val title: String,
    val walkability: Any
)