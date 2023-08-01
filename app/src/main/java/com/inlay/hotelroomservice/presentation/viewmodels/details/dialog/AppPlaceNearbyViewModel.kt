package com.inlay.hotelroomservice.presentation.viewmodels.details.dialog

import android.util.Log
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
import kotlinx.coroutines.flow.MutableStateFlow

class AppPlaceNearbyViewModel : PlaceNearbyViewModel() {
    private val _placesNearbyData: MutableStateFlow<NearbyPlace?> = MutableStateFlow(null)
    override val placesNearbyData = _placesNearbyData

    override fun initializeData(data: NearbyPlace) {
        _placesNearbyData.value = data
    }
}