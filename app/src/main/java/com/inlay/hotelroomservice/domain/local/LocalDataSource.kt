package com.inlay.hotelroomservice.domain.local

import com.inlay.hotelroomservice.data.local.models.HotelsItemEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertRepo(hotelsData: List<HotelsItemEntity>)
    suspend fun fetchRepo(): Flow<List<HotelsItemEntity>>
}