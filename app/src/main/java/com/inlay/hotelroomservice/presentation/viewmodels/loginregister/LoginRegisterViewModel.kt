package com.inlay.hotelroomservice.presentation.viewmodels.loginregister

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.StateFlow

abstract class LoginRegisterViewModel : ViewModel() {
    abstract val auth: StateFlow<FirebaseAuth?>

    abstract val userMail: LiveData<String>
    abstract val userPassword: LiveData<String>
    abstract val userFullName: LiveData<String>

    abstract val supportEmailText: LiveData<String>
    abstract val supportPasswordText: LiveData<String>
    abstract val supportFullNameText: LiveData<String>

    abstract val toastErrorMessage: StateFlow<String>

    abstract val isRememberChecked: StateFlow<Boolean>

    abstract fun initialize(
        close: () -> Unit,
        onSuggestionClicked: () -> Unit,
        navigateToProfile: () -> Unit,
        signOutOnRememberFalse: (Boolean) -> Unit,
        auth: FirebaseAuth
    )

    abstract fun close()

    abstract fun onSuggestionClicked()

    abstract fun onEmailTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
    abstract fun onPasswordTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
    abstract fun onFullNameTextChanged(s: CharSequence, start: Int, before: Int, count: Int)

    abstract fun onCheckedChanged(checked: Boolean)

    abstract fun login()

    abstract fun register()

    abstract fun isEmailValid(email: String): Boolean
}