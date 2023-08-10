package com.inlay.hotelroomservice.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inlay.hotelroomservice.data.local.database.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.database.models.HotelsItemStaysEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelsRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRepo(repos: List<HotelsItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveStayRepo(repo: HotelsItemStaysEntity)

    @Delete
    fun deleteStayRepo(repo: HotelsItemStaysEntity)

    @Query("SELECT * FROM hotels")
    fun fetchRepos(): Flow<List<HotelsItemEntity>>


    @Query("SELECT * FROM stays")
    fun fetchStaysRepos(): Flow<List<HotelsItemStaysEntity>>
}