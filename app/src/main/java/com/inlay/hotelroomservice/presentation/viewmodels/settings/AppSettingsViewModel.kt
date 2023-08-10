package com.inlay.hotelroomservice.presentation.viewmodels.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppSettingsViewModel(private val repositoryUseCase: RepositoryUseCase) : SettingsViewModel() {
    private val _langsList = MutableStateFlow(listOf<String>())
    override val langsList = _langsList

    private lateinit var _openLangDialog: () -> Unit

    override fun initialize(openLangDialog: () -> Unit, languagesList: List<String>) {
        _langsList.value = languagesList

        _openLangDialog = openLangDialog
    }

    override fun openLangDialog() {
        _openLangDialog()
    }

    override fun onDarkModeChanged(isChecked: Boolean) {
        viewModelScope.launch {
            val modeState = if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else AppCompatDelegate.MODE_NIGHT_NO
            repositoryUseCase.saveNightModeState(modeState)
        }
    }
}