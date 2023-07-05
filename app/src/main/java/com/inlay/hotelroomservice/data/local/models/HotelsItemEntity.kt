package com.inlay.hotelroomservice.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.inlay.hotelroomservice.data.local.StringListTypeConverter

@Entity(tableName = "hotels")
data class HotelsItemEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val hotelInfo: String,
    val rating: String,
    val price: String,
    @TypeConverters(StringListTypeConverter::class)
    val cardPhotos: List<String>?
)
