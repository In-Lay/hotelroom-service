package com.inlay.hotelroomservice.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.inlay.hotelroomservice.data.local.database.dao.HotelsRoomDao
import com.inlay.hotelroomservice.data.local.database.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.database.models.HotelsItemStaysEntity
import com.inlay.hotelroomservice.data.local.database.typeconverters.StringListTypeConverter

@Database(
    entities = [HotelsItemEntity::class, HotelsItemStaysEntity::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(StringListTypeConverter::class)
abstract class HotelsRoomDatabase : RoomDatabase() {
    abstract fun hotelsRoomDao(): HotelsRoomDao
}
