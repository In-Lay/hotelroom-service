package com.inlay.hotelroomservice.domain.local

import com.inlay.hotelroomservice.data.local.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.models.HotelsItemWithRatingEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertRepo(hotelsData: List<HotelsItemEntity>)
    suspend fun fetchRepo(): Flow<List<HotelsItemWithRatingEntity>>
}