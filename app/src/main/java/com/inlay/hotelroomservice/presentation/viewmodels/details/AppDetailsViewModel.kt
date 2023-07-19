package com.inlay.hotelroomservice.presentation.viewmodels.details

import androidx.databinding.BindingAdapter
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppDetailsViewModel(private val repositoryUseCase: RepositoryUseCase) : DetailsViewModel() {
    private val _selectedItem = MutableStateFlow(0)
    override val selectedItem = _selectedItem

    private val _hotelImage = MutableStateFlow("")
    override val hotelImage = _hotelImage.asLiveData()

    private val _hotelName = MutableStateFlow("")
    override val hotelName = _hotelName.asLiveData()

    private val _hotelAddress = MutableStateFlow("")
    override val hotelAddress = _hotelAddress.asLiveData()

    private val _hotelRating = MutableStateFlow("")
    override val hotelRating = _hotelRating.asLiveData()

    private val _hotelRatingCount = MutableStateFlow("")
    override val hotelRatingCount = _hotelRatingCount.asLiveData()


    private val _chipParkingText = MutableStateFlow("")
    override val chipParkingText = _chipParkingText.asLiveData()

    private val _chipInternetText = MutableStateFlow("")
    override val chipInternetText = _chipInternetText.asLiveData()

    private val _chipGymText = MutableStateFlow("")
    override val chipGymText = _chipGymText.asLiveData()

    private val _chipFoodText = MutableStateFlow("")
    override val chipFoodText = _chipFoodText.asLiveData()


    private val _hotelPrice = MutableStateFlow("")
    override val hotelPrice = _hotelPrice.asLiveData()

    private val _hotelProvider = MutableStateFlow("")
    override val hotelProvider = _hotelProvider.asLiveData()

    private val _hotelLink = MutableStateFlow("")
    override val hotelLink = _hotelLink.asLiveData()

    private val _hotelAbout = MutableStateFlow("")
    override val hotelAbout = _hotelAbout.asLiveData()


    private val _restaurantNearbyPhoto = MutableStateFlow("")
    override val restaurantNearbyPhoto = _restaurantNearbyPhoto.asLiveData()

    private val _restaurantNearbyName = MutableStateFlow("")
    override val restaurantNearbyName = _restaurantNearbyName.asLiveData()

    private val _restaurantNearbyInfo = MutableStateFlow("")
    override val restaurantNearbyInfo = _restaurantNearbyInfo.asLiveData()

    private val _restaurantNearbyRating = MutableStateFlow("")
    override val restaurantNearbyRating = _restaurantNearbyRating.asLiveData()

    private val _restaurantNearbyRatingCount = MutableStateFlow("")
    override val restaurantNearbyRatingCount = _restaurantNearbyRatingCount.asLiveData()

    private val _restaurantNearbyDistance = MutableStateFlow("")
    override val restaurantNearbyDistance = _restaurantNearbyDistance.asLiveData()


    private val _attractionNearbyPhoto = MutableStateFlow("")
    override val attractionNearbyPhoto = _attractionNearbyPhoto.asLiveData()

    private val _attractionNearbyName = MutableStateFlow("")
    override val attractionNearbyName = _attractionNearbyName.asLiveData()

    private val _attractionNearbyInfo = MutableStateFlow("")
    override val attractionNearbyInfo = _attractionNearbyInfo.asLiveData()

    private val _attractionNearbyRating = MutableStateFlow("")
    override val attractionNearbyRating = _attractionNearbyRating.asLiveData()

    private val _attractionNearbyRatingCount = MutableStateFlow("")
    override val attractionNearbyRatingCount = _attractionNearbyRatingCount.asLiveData()

    private val _attractionNearbyDistance = MutableStateFlow("")
    override val attractionNearbyDistance = _attractionNearbyDistance.asLiveData()

    private lateinit var openImageDialogLambda: () -> Unit
    private lateinit var viewAllRestaurantsLambda: () -> Unit
    private lateinit var viewAllAttractionsLambda: () -> Unit

    override fun openImageDialog() {
        openImageDialogLambda()
    }

    override fun viewAllRestaurants() {
        viewAllRestaurantsLambda()
    }

    override fun viewAllAttractions() {
        viewAllAttractionsLambda()
    }

    override fun initializeData(
        openImageDialog: () -> Unit,
        viewAllRestaurants: () -> Unit,
        viewAllAttractions: () -> Unit
    ) {
        openImageDialogLambda = openImageDialog
        viewAllRestaurantsLambda = viewAllRestaurants
        viewAllAttractionsLambda = viewAllAttractions
    }

    init {
//        viewModelScope.launch {
//            val hotelDetailsData = repositoryUseCase.getHotelDetailsRepo()
//        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter("detailsHotelImage")
        fun loadHotelImage(view: ShapeableImageView, source: String?) {
            view.shapeAppearanceModel =
                view.shapeAppearanceModel.toBuilder().setAllCornerSizes(50F).build()
            view.load(source)
        }

        @JvmStatic
        @BindingAdapter("detailsRestaurantNearbyImage")
        fun loadRestaurantNearbyImage(view: ShapeableImageView, source: String?) {
            view.shapeAppearanceModel =
                view.shapeAppearanceModel.toBuilder().setAllCornerSizes(50F).build()
            view.load(source)
        }

        @JvmStatic
        @BindingAdapter("detailsAttractionNearbyImage")
        fun loadAttractionNearbyImage(view: ShapeableImageView, source: String?) {
            view.shapeAppearanceModel =
                view.shapeAppearanceModel.toBuilder().setAllCornerSizes(50F).build()
            view.load(source)
        }
    }
}