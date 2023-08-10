package com.inlay.hotelroomservice.presentation.viewmodels.userstays

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

abstract class UserStaysViewModel : ViewModel() {
    abstract val isUserLogged: StateFlow<Boolean>

    abstract val user: StateFlow<FirebaseUser?>

    abstract val profileHeaderText: LiveData<String>
    abstract val profileUsernameText: LiveData<String>
    abstract val profileImage: LiveData<Uri?>

    abstract val userProfileData: StateFlow<String>

    abstract fun initializeData(
        goToHotels: () -> Unit,
        goToProfile: () -> Unit,
        isUserLogged: Boolean,
        user: FirebaseUser?,
        dayTime: String,
        loginMsg: String
    )

    abstract fun goToHotels()

    abstract fun goToProfile()
}