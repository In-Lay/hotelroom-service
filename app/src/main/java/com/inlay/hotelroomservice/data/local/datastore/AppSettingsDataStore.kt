package com.inlay.hotelroomservice.data.local.datastore

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.edit
import com.inlay.hotelroomservice.extensions.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppSettingsDataStore(private val context: Context) : SettingsDataStore {

    override suspend fun saveNightModeState(modeState: Int) {
        context.dataStore.edit {
            it[DataStoreKeys.DARK_MODE_KEY] = modeState
        }
    }

    override suspend fun saveNotificationsState(state: Boolean) {
        context.dataStore.edit {
            it[DataStoreKeys.NOTIFICATIONS_KEY] = state
        }
    }


    override fun getNightModeState(): Flow<Int> {
        return context.dataStore.data.map {
            it[DataStoreKeys.DARK_MODE_KEY] ?: AppCompatDelegate.MODE_NIGHT_NO
        }
    }

    override fun getNotificationsState(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[DataStoreKeys.NOTIFICATIONS_KEY] ?: true
        }
    }
}