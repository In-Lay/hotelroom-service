package com.inlay.hotelroomservice.data.local

import androidx.room.TypeConverter

class StringListTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<String> = value.split(",")

    @TypeConverter
    fun toString(list: List<String>): String = list.joinToString(",")
}