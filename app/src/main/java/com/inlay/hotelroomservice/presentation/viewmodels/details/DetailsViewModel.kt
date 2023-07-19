package com.inlay.hotelroomservice.presentation.viewmodels.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import kotlinx.coroutines.flow.StateFlow

abstract class DetailsViewModel : ViewModel() {
    abstract val selectedItem: StateFlow<Int>

    abstract val hotelImage: LiveData<String>
    abstract val hotelName: LiveData<String>
    abstract val hotelAddress: LiveData<String>
    abstract val hotelRating: LiveData<String>
    abstract val hotelRatingCount: LiveData<String>

    abstract val chipParkingText: LiveData<String>
    abstract val chipInternetText: LiveData<String>
    abstract val chipGymText: LiveData<String>
    abstract val chipFoodText: LiveData<String>

    abstract val hotelPrice: LiveData<String>
    abstract val hotelProvider: LiveData<String>
    abstract val hotelLink: LiveData<String>
    abstract val hotelAbout: LiveData<String>

    abstract val restaurantNearbyPhoto: LiveData<String>
    abstract val restaurantNearbyName: LiveData<String>
    abstract val restaurantNearbyInfo: LiveData<String>
    abstract val restaurantNearbyRating: LiveData<String>
    abstract val restaurantNearbyRatingCount: LiveData<String>
    abstract val restaurantNearbyDistance: LiveData<String>

    abstract val attractionNearbyPhoto: LiveData<String>
    abstract val attractionNearbyName: LiveData<String>
    abstract val attractionNearbyInfo: LiveData<String>
    abstract val attractionNearbyRating: LiveData<String>
    abstract val attractionNearbyRatingCount: LiveData<String>
    abstract val attractionNearbyDistance: LiveData<String>

    abstract fun openImageDialog()
    abstract fun viewAllRestaurants()
    abstract fun viewAllAttractions()

    abstract fun initializeData(
        openImageDialog: () -> Unit,
        viewAllRestaurants: () -> Unit,
        viewAllAttractions: () -> Unit
    )
}