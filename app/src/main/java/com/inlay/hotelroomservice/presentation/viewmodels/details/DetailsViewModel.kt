package com.inlay.hotelroomservice.presentation.viewmodels.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import kotlinx.coroutines.flow.StateFlow

abstract class DetailsViewModel : ViewModel() {
//    abstract val selectedItemId: StateFlow<String>
//    abstract val selectedDates: StateFlow<DatesModel>
//    abstract val selectedCurrency: StateFlow<String>

    abstract val hotelImagesList: StateFlow<List<String>>

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

    abstract val restaurantsNearby: StateFlow<List<NearbyPlace.RestaurantNearby>>
    abstract val attractionsNearby: StateFlow<List<NearbyPlace.AttractionNearby>>

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
    abstract fun openLinkInBrowser()
    abstract fun closeWebView()

    abstract fun initializeData(
        openImageDialog: () -> Unit,
        viewAllRestaurants: () -> Unit,
        viewAllAttractions: () -> Unit,
        openLinkInBrowser: (String) -> Unit,
        closeWebView: () -> Unit,
        id: String,
        dates: DatesModel,
        currency: String
    )
}