package com.inlay.hotelroomservice.data.local.datastore

import kotlinx.coroutines.flow.Flow

interface SettingsDataStore {
    suspend fun saveLanguage(langCode: String)

    suspend fun saveNightModeState(modeState: Int)

    suspend fun saveNotificationsState(state: Boolean)


    fun getLanguage(): Flow<String>

    fun getNightModeState(): Flow<Int>

    fun getNotificationsState(): Flow<Boolean>
}