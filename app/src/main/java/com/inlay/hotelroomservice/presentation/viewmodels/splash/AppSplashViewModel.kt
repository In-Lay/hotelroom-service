package com.inlay.hotelroomservice.presentation.viewmodels.splash

import androidx.lifecycle.viewModelScope
import com.inlay.hotelroomservice.domain.usecase.datastore.nightmode.GetNightMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppSplashViewModel(private val getNightMode: GetNightMode) :
    SplashViewModel() {
    private val _nightModeState = MutableStateFlow(0)
    override val nightModeState = _nightModeState

    init {
        viewModelScope.launch {
            getNightMode().collect {
                _nightModeState.value = it
            }
        }
    }
}