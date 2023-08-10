package com.inlay.hotelroomservice.presentation

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.LocaleList
import java.util.Locale


class LocaleContextWrapper(context: Context) : ContextWrapper(context) {
    companion object {
        fun wrap(context: Context?, newLocale: Locale): ContextWrapper {
            var changeContext = context
            val res = changeContext?.resources
            val configuration = res?.configuration

            configuration?.setLocale(newLocale)
            val localeList = LocaleList(newLocale)
            LocaleList.setDefault(localeList)
            configuration?.setLocales(localeList)

            changeContext = configuration?.let { changeContext?.createConfigurationContext(it) }
            return ContextWrapper(changeContext)
        }
    }
}