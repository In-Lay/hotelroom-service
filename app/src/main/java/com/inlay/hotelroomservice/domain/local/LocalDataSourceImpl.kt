package com.inlay.hotelroomservice.domain.local

import com.inlay.hotelroomservice.data.local.dao.HotelsRoomDao
import com.inlay.hotelroomservice.data.local.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.models.HotelsItemWithRatingEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val hotelsRoomDao: HotelsRoomDao) : LocalDataSource {
    override suspend fun insertRepo(hotelsData: List<HotelsItemEntity>) {
        hotelsRoomDao.saveRepo(hotelsData)
    }

    override suspend fun fetchRepo(): Flow<List<HotelsItemWithRatingEntity>> {
        return hotelsRoomDao.fetchRepos()
    }
}