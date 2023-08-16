package com.inlay.hotelroomservice.presentation.viewmodels.splash

import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.SaveNotificationsState

abstract class SplashViewModel : ViewModel() {
    abstract suspend fun saveNotificationState(notificationsState: Boolean)
}