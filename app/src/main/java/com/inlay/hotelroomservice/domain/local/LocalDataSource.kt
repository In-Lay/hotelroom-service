package com.inlay.hotelroomservice.domain.local

import com.inlay.hotelroomservice.data.local.database.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.database.models.HotelsItemStaysEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertRepo(hotelsData: List<HotelsItemEntity>)
    suspend fun fetchRepo(): Flow<List<HotelsItemEntity>>

    suspend fun insertStayRepo(repo: HotelsItemStaysEntity)

    suspend fun deleteStayRepo(repo: HotelsItemStaysEntity)

    suspend fun fetchStaysRepo(): Flow<List<HotelsItemStaysEntity>>


    suspend fun saveNightModeState(modeState: Int)

    suspend fun saveNotificationsState(state: Boolean)


    suspend fun getNightModeState(): Flow<Int>

    suspend fun getNotificationsState(): Flow<Boolean>
}