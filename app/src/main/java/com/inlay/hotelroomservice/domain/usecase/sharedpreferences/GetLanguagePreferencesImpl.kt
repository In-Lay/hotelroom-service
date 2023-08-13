package com.inlay.hotelroomservice.domain.usecase.sharedpreferences

import android.content.SharedPreferences

private const val LANG_KEY = "LANGUAGE_KEY"

class GetLanguagePreferencesImpl(private val sharedPreferences: SharedPreferences) :
    GetLanguagePreferences {
    override fun getLanguage(): String? {
        return sharedPreferences.getString(LANG_KEY, "en")
    }
}