package com.inlay.hotelroomservice.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HotelRatingEntity(
    @PrimaryKey(autoGenerate = true)
    val ratingId: Int,
    val count: String,
    val rating: Double
)
