package com.inlay.hotelroomservice.presentation.viewmodels.splash

import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.SaveNotificationsState
import kotlinx.coroutines.flow.StateFlow

abstract class SplashViewModel : ViewModel() {

    abstract val nightModeState: StateFlow<Int>
}