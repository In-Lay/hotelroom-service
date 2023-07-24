package com.inlay.hotelroomservice.presentation.viewmodels.details.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace

abstract class PlaceNearbyViewModel : ViewModel() {
    abstract val placeNearbyPhoto: LiveData<String>
    abstract val placeNearbyName: LiveData<String>
    abstract val placeNearbyInfo: LiveData<String>
    abstract val placeNearbyRating: LiveData<String>
    abstract val placeNearbyRatingCount: LiveData<String>
    abstract val placeNearbyDistance: LiveData<String>

    abstract fun initializeData(data: NearbyPlace)
}