package com.inlay.hotelroomservice.data.remote.models.hotels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String?,
    @Json(name = "primaryInfo") val primaryInfo: String?,
    @Json(name = "secondaryInfo") val secondaryInfo: String?,
    @Json(name = "bubbleRating") val bubbleRating: BubbleRating?,
    @Json(name = "provider") val provider: String?,
    @Json(name = "priceForDisplay") val priceForDisplay: String?,
    @Json(name = "cardPhotos") val cardPhotos: List<CardPhoto>
)