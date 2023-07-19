package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.inlay.hotelroomservice.data.local.models.hoteldetails.GettingThere
import com.inlay.hotelroomservice.data.local.models.hoteldetails.Neighborhood
import com.squareup.moshi.Json

data class Location(
    @Json(name = "address") val address: String,
    @Json(name = "gettingThere") val gettingThere: GettingThere,
    @Json(name = "neighborhood") val neighborhood: Neighborhood,
    @Json(name = "title") val title: String,
    @Json(name = "walkability") val walkability: String?
)