package com.inlay.hotelroomservice.presentation.viewmodels.editprofile

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

    override val emailChanged = MutableStateFlow(false)
    override val fullNameChanged = MutableStateFlow(false)

    private val _changesApplied = MutableStateFlow(true)
    override val changesApplied = _changesApplied

    private lateinit var _showAuthDialog: () -> Unit
    private lateinit var _showPhotoPicker: () -> Unit
    private lateinit var _saveAndGoBack: () -> Unit

    override fun initialize(
        user: FirebaseUser?,
        showAuthDialog: () -> Unit,
        showPhotoPicker: () -> Unit,
        saveAndGoBack: () -> Unit
    ) {
        _user.value = user

        _showAuthDialog = showAuthDialog
        _showPhotoPicker = showPhotoPicker
        _saveAndGoBack = saveAndGoBack

        _userPhoto.value = user?.photoUrl
        _fullName.value = user?.displayName.toString()
        _email.value = user?.email.toString()
    }

    override fun onFullNameChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        fullNameChanged.value = true
        _changesApplied.value = false
        _fullName.value = s.toString()
    }

    override fun onEmailChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        emailChanged.value = true
        _changesApplied.value = false
        if (!isEmailValid(s.toString())) {
            _supportEmailText.value = "Invalid Email"
        } else {
            _email.value = s.toString()
            _supportEmailText.value = ""
        }
    }

    override fun changePhoto() {
        _showPhotoPicker()
    }

    override fun save() {

        if (_changesApplied.value) {
            _toastText.value = "You have changed nothing!"
        } else {
            if (fullNameChanged.value) {
                changeFullName()
            } else if (emailChanged.value) {
                _showAuthDialog()
            }
            _saveAndGoBack()
        }
    }

    override fun changeFullName() {
        val profileUpdate = UserProfileChangeRequest.Builder()

        profileUpdate.displayName = _fullName.value

        _user.value?.updateProfile(profileUpdate.build())?.addOnCompleteListener {
            if (it.isSuccessful) {
                _changesApplied.value = true
                _toastText.value = "Changes applied"
            }
        }
    }

    override fun changeEmail(email: String, password: String) {
        val credentials = EmailAuthProvider.getCredential(email, password)

        _user.value?.reauthenticate(credentials)?.addOnCompleteListener { auth ->
            if (auth.isSuccessful) {
                _user.value = Firebase.auth.currentUser
                _user.value?.updateEmail(_email.value)!!.addOnCompleteListener { update ->
                    if (update.isSuccessful) {
                        _toastText.value = "Changes applied"
                    }
                }.addOnFailureListener {
                    _toastText.value = it.message.toString()
                }
            }
        }?.addOnFailureListener {
            _toastText.value = it.message.toString()
        }
    }

    override fun changePhoto(uri: Uri) {
        _changesApplied.value = false
        _userPhoto.value = uri

        val fireStoreReference = Firebase.storage.reference.child(uri.path!!)

        val uploadTask = fireStoreReference.putFile(uri)

        uploadTask.continueWithTask {
            fireStoreReference.downloadUrl
        }.addOnCompleteListener { upload ->
            if (upload.isSuccessful) {
                val profileUpdate = UserProfileChangeRequest.Builder()

                profileUpdate.photoUri = upload.result

                _user.value?.updateProfile(profileUpdate.build())?.addOnCompleteListener { update ->
                    if (update.isSuccessful) {
                        _changesApplied.value = true
                        _toastText.value = "Changes applied"
                    }
                }
            }
        }.addOnFailureListener {
            _toastText.value = it.message.toString()
        }
    }

    override fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}