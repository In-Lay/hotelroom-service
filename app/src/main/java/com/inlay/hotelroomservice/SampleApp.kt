package com.inlay.hotelroomservice

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.inlay.hotelroomservice.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.auth
        Firebase.storage
        Firebase.database

        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@SampleApp)
            modules(appModule)
        }
    }
}