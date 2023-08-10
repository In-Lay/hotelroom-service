package com.inlay.hotelroomservice.domain.local

import com.inlay.hotelroomservice.data.local.database.dao.HotelsRoomDao
import com.inlay.hotelroomservice.data.local.database.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.database.models.HotelsItemStaysEntity
import com.inlay.hotelroomservice.data.local.datastore.SettingsDataStore
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(
    private val hotelsRoomDao: HotelsRoomDao,
    private val settingsDataStore: SettingsDataStore
) : LocalDataSource {
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


    override suspend fun saveLanguage(langCode: String) {
        settingsDataStore.saveLanguage(langCode)
    }

    override suspend fun saveNightModeState(modeState: Int) {
        settingsDataStore.saveNightModeState(modeState)
    }

    override suspend fun saveNotificationsState(state: Boolean) {
        settingsDataStore.saveNotificationsState(state)
    }

    override suspend fun getLanguage(): Flow<String> {
        return settingsDataStore.getLanguage()
    }

    override suspend fun getNightModeState(): Flow<Int> {
        return settingsDataStore.getNightModeState()
    }

    override suspend fun getNotificationsState(): Flow<Boolean> {
        return settingsDataStore.getNotificationsState()
    }
}