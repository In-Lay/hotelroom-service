package com.inlay.hotelroomservice.presentation.viewmodels.details.dialog

import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
import kotlinx.coroutines.flow.StateFlow

abstract class PlaceNearbyViewModel : ViewModel() {
    abstract val placesNearbyData: StateFlow<NearbyPlace?>

    abstract fun initializeData(data: NearbyPlace)
}