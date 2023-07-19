package com.inlay.hotelroomservice.presentation.viewmodels.userstays

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class UserStaysViewModel : ViewModel() {
    abstract val isUserLogged: StateFlow<Boolean>

    abstract val profileHeaderText: LiveData<String>
    abstract val profileUsernameText: LiveData<String>
    abstract val profileImage: LiveData<String>

    abstract val userProfileData: StateFlow<String>

    abstract fun initializeData(goToHotels: () -> Unit, isUserLogged: Boolean)

    abstract fun goToHotels()
}