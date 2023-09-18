package com.inlay.hotelroomservice.presentation.viewmodels.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import com.inlay.hotelroomservice.domain.usecase.datastore.nightmode.SaveNightMode
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.GetNotificationsState
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.SaveNotificationsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppSettingsViewModel(
    private val saveNightMode: SaveNightMode,
    private val getNotificationsState: GetNotificationsState,
    private val saveNotificationsState: SaveNotificationsState
) : SettingsViewModel() {
    private val _langsList = MutableStateFlow(listOf<String>())
    override val langsList = _langsList

    private val _notificationsState = MutableStateFlow(false)
    override val inAppNotificationsState = _notificationsState

    private lateinit var _openLangDialog: () -> Unit

    init {
        viewModelScope.launch {
            getNotificationsState().collect {
                _notificationsState.value = it
            }
        }
    }

    override fun initialize(openLangDialog: () -> Unit, languagesList: List<String>) {
        _langsList.value = languagesList

        _openLangDialog = openLangDialog
    }

    override fun openLangDialog() {
        _openLangDialog()
    }

    override fun changeNotificationsState(state: Boolean) {
        _notificationsState.value = state
        viewModelScope.launch {
            saveNotificationsState(state)
        }
    }

    override fun onDarkModeChanged(isChecked: Boolean) {
        viewModelScope.launch {
            val modeState = if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else AppCompatDelegate.MODE_NIGHT_NO
            saveNightMode(modeState)
        }
    }

    override fun onNotificationsSwitchChanged(isChecked: Boolean) {
        viewModelScope.launch {
            saveNotificationsState(isChecked)
        }
    }
}