package com.inlay.hotelroomservice.data.local.models.hoteldetails

import com.squareup.moshi.Json

data class Price(
    @Json(name = "price") val displayPrice: String?,
    @Json(name = "strikeThroughPrice") val strikeThroughPrice: String?,
    @Json(name = "status") val status: String,
    @Json(name = "providerName") val providerName: String,
    @Json(name = "freeCancellation") val freeCancellation: String?,
    @Json(name = "pricingPeriod") val pricingPeriod: String?
)