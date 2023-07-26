package com.inlay.hotelroomservice.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.inlay.hotelroomservice.data.local.dao.HotelsRoomDao
import com.inlay.hotelroomservice.data.local.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.typeconverters.StringListTypeConverter

@Database(
    entities = [HotelsItemEntity::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(StringListTypeConverter::class)
abstract class HotelsRoomDatabase : RoomDatabase() {
    abstract fun hotelsRoomDao(): HotelsRoomDao
}
