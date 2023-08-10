package com.inlay.hotelroomservice.presentation

import android.content.Context
import android.os.LocaleList
import java.util.Locale

object SetLocale {
    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = resources.configuration

        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)

        context.createConfigurationContext(configuration)
    }
}
