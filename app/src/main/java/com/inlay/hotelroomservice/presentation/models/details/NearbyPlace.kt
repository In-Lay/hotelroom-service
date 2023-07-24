package com.inlay.hotelroomservice.presentation.models.details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface NearbyPlace {
    val title: String
    val primaryInfo: String
    val distance: String
    val photoUrlTemplate: String
    val rating: Double
    val numberReviews: String

    @Parcelize
    data class RestaurantNearby(
        override val title: String,
        override val primaryInfo: String,
        override val distance: String,
        override val photoUrlTemplate: String,
        override val rating: Double,
        override val numberReviews: String
    ) : NearbyPlace, Parcelable

    @Parcelize
    data class AttractionNearby(
        override val title: String,
        override val primaryInfo: String,
        override val distance: String,
        override val photoUrlTemplate: String,
        override val rating: Double,
        override val numberReviews: String
    ) : NearbyPlace, Parcelable
}
