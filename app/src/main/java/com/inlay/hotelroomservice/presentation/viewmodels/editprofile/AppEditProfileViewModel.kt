package com.inlay.hotelroomservice.presentation.viewmodels.editprofile

import android.net.Uri
import androidx.core.util.PatternsCompat
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.annotations.TestOnly

class AppEditProfileViewModel(private val getAuthUser: GetAuthUser) : EditProfileViewModel() {
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

    private val _emailChanged = MutableStateFlow(false)
    override val emailChanged = _emailChanged

    private val _fullNameChanged = MutableStateFlow(false)
    override val fullNameChanged = _fullNameChanged

    private val _changesApplied = MutableStateFlow(true)
    override val changesApplied = _changesApplied

    private lateinit var _showAuthDialog: () -> Unit
    private lateinit var _showPhotoPicker: () -> Unit

    override fun initialize(
        user: FirebaseUser?,
        showAuthDialog: () -> Unit,
        showPhotoPicker: () -> Unit
    ) {
        _user.value = user

        _showAuthDialog = showAuthDialog
        _showPhotoPicker = showPhotoPicker

        _userPhoto.value = user?.photoUrl
        _fullName.value = user?.displayName.toString()
        _email.value = user?.email.toString()
    }

    override fun onFullNameChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        _fullNameChanged.value = true
        _changesApplied.value = false
        _fullName.value = s.toString()
    }

    override fun onEmailChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        _emailChanged.value = true
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
            if (_fullNameChanged.value) {
                changeFullName()
            }
            if (_emailChanged.value) {
                _showAuthDialog()
            }
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
                _user.value = getAuthUser()
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

    override fun changePhoto(uri: Uri, fireStoreReference: StorageReference) {
        _changesApplied.value = false
        _userPhoto.value = uri

        fireStoreReference.child(uri.path!!)

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
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    @TestOnly
    override fun changeUserData(user: FirebaseUser, userMail: String, userName: String) {
        _user.value = user
        _email.value = userMail
        _fullName.value = userName
    }

    override fun editChangesState(
        isEmailChanged: Boolean,
        isFullNameChanged: Boolean,
        isChangesApplied: Boolean
    ) {
        _changesApplied.value = isChangesApplied
        _emailChanged.value = isEmailChanged
        _fullNameChanged.value = isFullNameChanged
    }
}