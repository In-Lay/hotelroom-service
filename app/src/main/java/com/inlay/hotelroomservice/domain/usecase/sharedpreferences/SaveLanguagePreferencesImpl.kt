package com.inlay.hotelroomservice.domain.usecase.sharedpreferences

import android.content.SharedPreferences

private const val LANG_KEY = "LANGUAGE_KEY"

class SaveLanguagePreferencesImpl(private val sharedPreferences: SharedPreferences) :
    SaveLanguagePreferences {
    override fun saveLanguage(langCode: String) {
        sharedPreferences.edit().putString(LANG_KEY, langCode).apply()
    }
}