package com.inlay.hotelroomservice.presentation.viewmodels.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class SettingsViewModel : ViewModel() {
    abstract val langsList: StateFlow<List<String>>

    abstract val inAppNotificationsState: StateFlow<Boolean>

    abstract fun initialize(openLangDialog: () -> Unit, languagesList: List<String>)

    abstract fun openLangDialog()

    abstract fun changeNotificationsState(state: Boolean)

    abstract fun onDarkModeChanged(isChecked: Boolean)

    abstract fun onNotificationsSwitchChanged(isChecked: Boolean)
}