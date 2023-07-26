package com.inlay.hotelroomservice.presentation.viewmodels.userstays

import androidx.databinding.BindingAdapter
import androidx.lifecycle.asLiveData
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.inlay.hotelroomservice.R
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar

class AppUserStaysViewModel : UserStaysViewModel() {
    private val _profileHeaderText = MutableStateFlow("")
    override val profileHeaderText = _profileHeaderText.asLiveData()

    private val _profileUsernameText = MutableStateFlow("")
    override val profileUsernameText = _profileUsernameText.asLiveData()

    private val _profileImage = MutableStateFlow("")
    override val profileImage = _profileImage.asLiveData()

    private val _isUserLogged = MutableStateFlow(false)
    override val isUserLogged = _isUserLogged

    private val _userProfileData = MutableStateFlow("")
    override val userProfileData = _userProfileData

    private lateinit var goToHotelsLambda: () -> Unit

    override fun initializeData(goToHotels: () -> Unit, isUserLogged: Boolean) {
        goToHotelsLambda = goToHotels
        _isUserLogged.value = isUserLogged
        _profileHeaderText.value = "Good ${getDayTime()}"
        _profileUsernameText.value = if (isUserLogged) {
            "${_userProfileData.value}!"
        } else "Please, Log in!"
    }

    override fun goToHotels() {
        goToHotelsLambda()
    }

    private fun getDayTime(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 1..4 -> "Night"
            in 4..9 -> "Morning"
            in 9..17 -> "Day"
            in 17..22 -> "Evening"
            in 22..24 -> "Night"
            else -> "Day"
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("profileImageSource")
        fun loadImage(view: ShapeableImageView, src: String?) {
            view.shapeAppearanceModel =
                view.shapeAppearanceModel.toBuilder().setAllCornerSizes(85F).build()
            if (src.isNullOrEmpty()) {
                view.load(R.drawable.baseline_person_24)
            } else view.load(src)
        }
    }
}