package com.inlay.hotelroomservice.domain.local

import com.inlay.hotelroomservice.data.local.dao.HotelsRoomDao
import com.inlay.hotelroomservice.data.local.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.models.HotelsItemStaysEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val hotelsRoomDao: HotelsRoomDao) : LocalDataSource {
    override suspend fun insertRepo(hotelsData: List<HotelsItemEntity>) {
        hotelsRoomDao.saveRepo(hotelsData)
    }

    override suspend fun fetchRepo(): Flow<List<HotelsItemEntity>> {
        return hotelsRoomDao.fetchRepos()
    }

    override suspend fun insertStayRepo(repo: HotelsItemStaysEntity) {
        return hotelsRoomDao.saveStayRepo(repo)
    }

    override suspend fun deleteStayRepo(repo: HotelsItemStaysEntity) {
        return hotelsRoomDao.deleteStayRepo(repo)
    }

    override suspend fun fetchStaysRepo(): Flow<List<HotelsItemStaysEntity>> {
        return hotelsRoomDao.fetchStaysRepos()
    }
}