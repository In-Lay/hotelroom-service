package com.inlay.hotelroomservice.presentation.viewmodels.editprofile

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow

class AppEditProfileViewModel : EditProfileViewModel() {
    private val _user: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    override val user = _user

    private val _userPhoto: MutableStateFlow<Uri?> = MutableStateFlow(null)
    override val userPhoto = _userPhoto.asLiveData()

    private val _fullName = MutableStateFlow("")
    override val fullName = _fullName.asLiveData()

    private val _email = MutableStateFlow("")
    override val email = _email.asLiveData()

    private val _supportEmailText = MutableStateFlow("")
    override val supportEmailText = _supportEmailText.asLiveData()

    private val _toastText = MutableStateFlow("")
    override val toastText = _toastText

    private val _changesApplied = MutableStateFlow(true)
    override val changesApplied = _changesApplied

    override fun initialize(user: FirebaseUser?) {
        _user.value = user

        _userPhoto.value = user?.photoUrl
        _fullName.value = user?.displayName.toString()
        _email.value = user?.email.toString()
    }

    override fun onFullNameChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        //TODO Log changes
        _changesApplied.value = false
        _fullName.value = s.toString()
    }

    override fun onEmailChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        _changesApplied.value = false
        if (!isEmailValid(s.toString())) {
            _supportEmailText.value = "Invalid Email"
        } else {
            _email.value = s.toString()
            _supportEmailText.value = ""
        }
    }

    override fun changePhoto() {
        _changesApplied.value = false
        _toastText.value = "Photo changed"
    }

    override fun save() {
        if (!_changesApplied.value) _toastText.value = "You have changed nothing!"
        else {
            //TODO Update Email
            val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(_fullName.value)
                .setPhotoUri(_userPhoto.value).build()
            _user.value?.updateProfile(profileUpdate)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    _changesApplied.value = true
                    _toastText.value = "Changes applied"
                }
            }
        }
    }

    override fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}