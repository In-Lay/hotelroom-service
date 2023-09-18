package com.inlay.hotelroomservice.presentation.viewmodels.profile

import android.net.Uri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseUser
import com.inlay.hotelroomservice.R
import kotlinx.coroutines.flow.MutableStateFlow

class AppProfileViewModel : ProfileViewModel() {
    private val _userName = MutableStateFlow("")
    override val userName = _userName.asLiveData()

    private val _userEmail = MutableStateFlow("")
    override val userEmail = _userEmail.asLiveData()

    private val _profileImage: MutableStateFlow<Uri?> = MutableStateFlow(null)
    override val profileImage = _profileImage.asLiveData()

    private lateinit var userLogout: () -> Unit
    private lateinit var editProfile: () -> Unit

    override fun initializeData(logout: () -> Unit, edit: () -> Unit, user: FirebaseUser?) {
        userLogout = logout
        editProfile = edit

        _userName.value = user?.displayName.toString()
        _userEmail.value = user?.email.toString()
        _profileImage.value = user?.photoUrl
    }

    override fun logout() {
        userLogout()
    }

    override fun edit() {
        editProfile()
    }

    companion object {

        @JvmStatic
        @BindingAdapter("profileImageSrc")
        fun loadImage(view: ShapeableImageView, url: Uri?) {
            if (url == null) {
                view.load(R.drawable.baseline_person_24)
                {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            } else {
                view.load(url) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }
}