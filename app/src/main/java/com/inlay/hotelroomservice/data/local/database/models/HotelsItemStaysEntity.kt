package com.inlay.hotelroomservice.data.local.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.inlay.hotelroomservice.data.local.database.typeconverters.StringListTypeConverter

@Entity(tableName = "stays")
data class HotelsItemStaysEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val hotelInfo: String,
    val rating: String,
    val ratingCount: String,
    val price: String,
    @TypeConverters(StringListTypeConverter::class)
    val cardPhotos: List<String>?
)