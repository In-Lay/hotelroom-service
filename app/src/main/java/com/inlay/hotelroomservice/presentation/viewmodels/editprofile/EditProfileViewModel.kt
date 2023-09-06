package com.inlay.hotelroomservice.presentation.viewmodels.editprofile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.annotations.TestOnly

abstract class EditProfileViewModel : ViewModel() {
    abstract val user: StateFlow<FirebaseUser?>

    abstract val userPhoto: LiveData<Uri?>
    abstract val fullName: LiveData<String>
    abstract val email: LiveData<String>

    abstract val supportEmailText: LiveData<String>

    abstract val toastText: StateFlow<String>

    abstract val emailChanged: StateFlow<Boolean>
    abstract val fullNameChanged: StateFlow<Boolean>
    abstract val changesApplied: StateFlow<Boolean>

    abstract fun initialize(
        user: FirebaseUser?,
        showAuthDialog: () -> Unit,
        showPhotoPicker: () -> Unit
    )

    abstract fun onFullNameChanged(s: CharSequence, start: Int, before: Int, count: Int)
    abstract fun onEmailChanged(s: CharSequence, start: Int, before: Int, count: Int)

    abstract fun changePhoto()

    abstract fun save()

    abstract fun changeFullName()

    abstract fun changeEmail(email: String, password: String)

    abstract fun changePhoto(uri: Uri, fireStoreReference: StorageReference)

    abstract fun isEmailValid(email: String): Boolean

    @TestOnly
    abstract fun changeUserData(user: FirebaseUser, userMail: String, userName: String)

    @TestOnly
    abstract fun editChangesState(
        isEmailChanged: Boolean,
        isFullNameChanged: Boolean,
        isChangesApplied: Boolean
    )
}