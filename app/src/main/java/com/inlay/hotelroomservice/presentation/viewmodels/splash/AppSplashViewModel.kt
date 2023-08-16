package com.inlay.hotelroomservice.presentation.viewmodels.splash

import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.SaveNotificationsState

class AppSplashViewModel(private val saveNotificationsState: SaveNotificationsState) :
    SplashViewModel() {
    override suspend fun saveNotificationState(notificationsState: Boolean) {
        saveNotificationsState(notificationsState)
    }
}