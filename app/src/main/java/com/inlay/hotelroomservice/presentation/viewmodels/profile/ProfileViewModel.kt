package com.inlay.hotelroomservice.presentation.viewmodels.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

abstract class ProfileViewModel : ViewModel() {
    abstract val userName: LiveData<String>
    abstract val userEmail: LiveData<String>
    abstract val profileImage: LiveData<Uri?>

    abstract fun initializeData(logout: () -> Unit, edit: () -> Unit, user:FirebaseUser?)

    abstract fun edit()

    abstract fun logout()
}