package com.inlay.hotelroomservice.presentation.viewmodels.details.dialog

import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
import kotlinx.coroutines.flow.MutableStateFlow

class AppPlaceNearbyViewModel : PlaceNearbyViewModel() {
    private val _placesNearbyData: MutableStateFlow<NearbyPlace?> = MutableStateFlow(null)
    override val placesNearbyData = _placesNearbyData

    override fun initializeData(data: NearbyPlace) {
        _placesNearbyData.value = data
    }
}