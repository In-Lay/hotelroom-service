package com.inlay.hotelroomservice.domain.local

import com.inlay.hotelroomservice.data.local.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.models.HotelsItemStaysEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertRepo(hotelsData: List<HotelsItemEntity>)
    suspend fun fetchRepo(): Flow<List<HotelsItemEntity>>

    suspend fun insertStayRepo(repo: HotelsItemStaysEntity)

    suspend fun deleteStayRepo(repo: HotelsItemStaysEntity)

    suspend fun fetchStaysRepo(): Flow<List<HotelsItemStaysEntity>>
}