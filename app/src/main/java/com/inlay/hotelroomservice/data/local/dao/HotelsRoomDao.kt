package com.inlay.hotelroomservice.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inlay.hotelroomservice.data.local.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.models.HotelsItemWithRatingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelsRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRepo(repos: List<HotelsItemEntity>)

    @Query("SELECT * FROM hotels")
    fun fetchRepos(): Flow<List<HotelsItemWithRatingEntity>>
}