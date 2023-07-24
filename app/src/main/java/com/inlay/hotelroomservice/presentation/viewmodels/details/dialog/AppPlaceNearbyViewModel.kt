package com.inlay.hotelroomservice.presentation.viewmodels.details.dialog

import androidx.lifecycle.asLiveData
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
import kotlinx.coroutines.flow.MutableStateFlow

class AppPlaceNearbyViewModel : PlaceNearbyViewModel() {
    private val _placeNearbyPhoto = MutableStateFlow("")
    override val placeNearbyPhoto = _placeNearbyPhoto.asLiveData()

    private val _placeNearbyName = MutableStateFlow("")
    override val placeNearbyName = _placeNearbyName.asLiveData()

    private val _placeNearbyInfo = MutableStateFlow("")
    override val placeNearbyInfo = _placeNearbyInfo.asLiveData()

    private val _placeNearbyRating = MutableStateFlow("")
    override val placeNearbyRating = _placeNearbyRating.asLiveData()

    private val _placeNearbyRatingCount = MutableStateFlow("")
    override val placeNearbyRatingCount = _placeNearbyRatingCount.asLiveData()

    private val _placeNearbyDistance = MutableStateFlow("")
    override val placeNearbyDistance = _placeNearbyDistance.asLiveData()

    override fun initializeData(data: NearbyPlace) {
        _placeNearbyPhoto.value = data.photoUrlTemplate.replace(
            "{width}", "500"
        ).replace("{height}", "300")
        _placeNearbyName.value = data.title
        _placeNearbyInfo.value = data.primaryInfo
        _placeNearbyRating.value = data.rating.toString()
        _placeNearbyRatingCount.value = "(${data.numberReviews})"
        _placeNearbyDistance.value = data.distance
    }
}