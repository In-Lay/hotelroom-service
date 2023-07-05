package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class AttractionsNearby(
    @Json(name = "content") val content: List<AttractionsNearbyContent>,
    @Json(name = "sectionTitle")  val sectionTitle: String
)