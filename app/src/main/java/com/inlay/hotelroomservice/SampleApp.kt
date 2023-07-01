package com.inlay.hotelroomservice

import android.app.Application
import com.inlay.hotelroomservice.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@SampleApp)
            modules(appModule)
        }
    }
}