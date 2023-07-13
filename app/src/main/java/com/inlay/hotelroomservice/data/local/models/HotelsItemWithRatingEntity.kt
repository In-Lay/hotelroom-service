package com.inlay.hotelroomservice.data.local.models

import androidx.room.Embedded
import androidx.room.Relation

data class HotelsItemWithRatingEntity(
    @Embedded
    val hotelItem: HotelsItemEntity,
    @Relation(parentColumn = "id", entityColumn = "ratingId")
    val rating: HotelRatingEntity?
)
