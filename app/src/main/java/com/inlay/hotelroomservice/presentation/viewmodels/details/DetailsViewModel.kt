package com.inlay.hotelroomservice.presentation.viewmodels.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import kotlinx.coroutines.flow.StateFlow

abstract class DetailsViewModel : ViewModel() {
    abstract val hotelDetailsData: LiveData<HotelDetailsUiModel?>

    abstract val hotelImagesList: StateFlow<List<String>>

    abstract val hotelImage: LiveData<String>
    abstract val hotelPrice: LiveData<String>

    abstract val chipParkingText: LiveData<String>
    abstract val chipInternetText: LiveData<String>
    abstract val chipGymText: LiveData<String>
    abstract val chipFoodText: LiveData<String>

    abstract val hotelProvider: LiveData<String>
    abstract val hotelLink: LiveData<String>
    abstract val hotelAbout: LiveData<String>

    abstract val restaurantsNearby: StateFlow<List<NearbyPlace.RestaurantNearby>>
    abstract val attractionsNearby: StateFlow<List<NearbyPlace.AttractionNearby>>

    abstract val restaurantNearbyPhoto: LiveData<String>

    abstract val attractionNearbyPhoto: LiveData<String>

    abstract fun openImageDialog()
    abstract fun viewAllRestaurants()
    abstract fun viewAllAttractions()
    abstract fun openLinkInBrowser()

    abstract fun initializeData(
        openImageDialog: () -> Unit,
        viewAllRestaurants: () -> Unit,
        viewAllAttractions: () -> Unit,
        openLinkInBrowser: (String) -> Unit,
        hotelDetailsSearchModel: HotelDetailsSearchModel
    )
}