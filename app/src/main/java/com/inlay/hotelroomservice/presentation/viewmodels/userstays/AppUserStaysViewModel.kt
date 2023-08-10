package com.inlay.hotelroomservice.presentation.viewmodels.userstays

import android.content.Context
import android.net.Uri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseUser
import com.inlay.hotelroomservice.R
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar

class AppUserStaysViewModel : UserStaysViewModel() {
    private val _user: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    override val user = _user

    private val _profileHeaderText = MutableStateFlow("")
    override val profileHeaderText = _profileHeaderText.asLiveData()

    private val _profileUsernameText = MutableStateFlow("")
    override val profileUsernameText = _profileUsernameText.asLiveData()

    private val _profileImage: MutableStateFlow<Uri?> = MutableStateFlow(null)
    override val profileImage = _profileImage.asLiveData()

    private val _isUserLogged = MutableStateFlow(false)
    override val isUserLogged = _isUserLogged

    private val _userProfileData = MutableStateFlow("")
    override val userProfileData = _userProfileData

    private lateinit var _goToHotels: () -> Unit
    private lateinit var _goToProfile: () -> Unit

    override fun initializeData(
        goToHotels: () -> Unit,
        goToProfile: () -> Unit,
        isUserLogged: Boolean,
        user: FirebaseUser?,
        dayTime: String,
        loginMsg: String
    ) {
        _user.value = user

        _goToHotels = goToHotels
        _goToProfile = goToProfile

        _isUserLogged.value = isUserLogged
        _profileHeaderText.value = dayTime
        _profileUsernameText.value = if (_isUserLogged.value) {
            "${_user.value?.displayName}!"
        } else loginMsg
        _profileImage.value = if (_isUserLogged.value) _user.value?.photoUrl else null
    }

    override fun goToHotels() {
        _goToHotels()
    }

    override fun goToProfile() {
        _goToProfile()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("profileImageSource")
        fun loadImage(view: ShapeableImageView, src: Uri?) {
//            view.shapeAppearanceModel =
//                view.shapeAppearanceModel.toBuilder().setAllCornerSizes(85F).build()
            if (src == null) {
                view.load(R.drawable.baseline_person_24) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            } else view.load(src) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
    }
}