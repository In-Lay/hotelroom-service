package com.inlay.hotelroomservice.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    val DARK_MODE_KEY = intPreferencesKey("NIGHT_MODE")
    val NOTIFICATIONS_KEY = booleanPreferencesKey("PUSH_NOTIFICATIONS")
}