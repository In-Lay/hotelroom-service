package com.inlay.hotelroomservice.presentation.viewmodels.details

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCase
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class AppDetailsViewModel(private val repositoryUseCase: RepositoryUseCase) : DetailsViewModel() {
    //    private val _selectedItemId = MutableStateFlow("")
//    override val selectedItemId = _selectedItemId
//
//    private val _selectedDates = MutableStateFlow(DatesModel("", ""))
//    override val selectedDates = _selectedDates
//
//    private val _selectedCurrency = MutableStateFlow("")
//    override val selectedCurrency = _selectedCurrency
    private val _hotelImagesList = MutableStateFlow(listOf<String>())
    override val hotelImagesList = _hotelImagesList


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


    private val _restaurantsNearby = MutableStateFlow(listOf<NearbyPlace.RestaurantNearby>())
    override val restaurantsNearby = _restaurantsNearby

    private val _attractionsNearby = MutableStateFlow(listOf<NearbyPlace.AttractionNearby>())
    override val attractionsNearby = _attractionsNearby


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
    private lateinit var openLinkInBrowserLambda: (String) -> Unit
    private lateinit var closeWebViewLambda: () -> Unit

    override fun openImageDialog() {
        openImageDialogLambda()
    }

    override fun viewAllRestaurants() {
        viewAllRestaurantsLambda()
    }

    override fun viewAllAttractions() {
        viewAllAttractionsLambda()
    }

    override fun openLinkInBrowser() {
        openLinkInBrowserLambda(_hotelLink.value)
    }

    override fun closeWebView() {
        closeWebViewLambda()
    }

    override fun initializeData(
        openImageDialog: () -> Unit,
        viewAllRestaurants: () -> Unit,
        viewAllAttractions: () -> Unit,
        openLinkInBrowser: (String) -> Unit,
        closeWebView: () -> Unit,
        id: String,
        dates: DatesModel,
        currency: String
    ) {
        openImageDialogLambda = openImageDialog
        viewAllRestaurantsLambda = viewAllRestaurants
        viewAllAttractionsLambda = viewAllAttractions
        openLinkInBrowserLambda = openLinkInBrowser
        closeWebViewLambda = closeWebView

//        _selectedItemId.value = id
//        _selectedDates.value = dates
//        _selectedCurrency.value = currency
        getDetailsData(id, dates, currency)
    }

    private fun getDetailsData(
        id: String, dates: DatesModel, currency: String
    ) {
        viewModelScope.launch {
            val hotelDetailsData = repositoryUseCase.getHotelDetailsRepo(
                id, dates.checkInDate, dates.checkOutDate, currency
            )

            assignDetailsDataToViews(hotelDetailsData)
        }
    }

    private fun assignDetailsDataToViews(hotelDetailsData: HotelDetailsUiModel) {
        _hotelImagesList.value = hotelDetailsData.photos

        _hotelImage.value =
            if (hotelDetailsData.photos.isNotEmpty()) hotelDetailsData.photos[0].replace(
                "{width}", "500"
            ).replace("{height}", "300") else ""

        _hotelName.value = hotelDetailsData.title
        _hotelAddress.value = hotelDetailsData.address
        _hotelRating.value = hotelDetailsData.rating
        _hotelRatingCount.value = "(${hotelDetailsData.numberReviews})"

        _chipParkingText.value =
            hotelDetailsData.aboutAmenities.getAmenityByTitle(AmenityTitles.PARKING.titles)
        _chipInternetText.value =
            hotelDetailsData.aboutAmenities.getAmenityByTitle(AmenityTitles.INTERNET.titles)
        _chipGymText.value =
            hotelDetailsData.aboutAmenities.getAmenityByTitle(AmenityTitles.FITNESS.titles)
        _chipFoodText.value =
            hotelDetailsData.aboutAmenities.getAmenityByTitle(AmenityTitles.BAR.titles)

        _hotelPrice.value = hotelDetailsData.displayPrice.ifEmpty {
            "Visit the website by Link below"
        }
        _hotelProvider.value = "by ${hotelDetailsData.providerName}"

        _hotelLink.value =
            if (hotelDetailsData.aboutLinks.isNotEmpty()) hotelDetailsData.aboutLinks[0] else ""
        _hotelAbout.value = hotelDetailsData.aboutTitle.ifEmpty {
            hotelDetailsData.rankingDetails
                .replace("#", "№")
                .replace(Regex("<a>|</a>"), "")
        }

        _restaurantsNearby.value = hotelDetailsData.restaurantsNearby
        _attractionsNearby.value = hotelDetailsData.attractionsNearby

        val firstRestaurantNearby = hotelDetailsData.restaurantsNearby[0]
        val firstAttractionNearby = hotelDetailsData.attractionsNearby[0]

        _restaurantNearbyPhoto.value =
            firstRestaurantNearby.photoUrlTemplate.replace("{width}", "500")
                .replace("{height}", "300")
        _restaurantNearbyName.value = firstRestaurantNearby.title
        _restaurantNearbyInfo.value = firstRestaurantNearby.primaryInfo
        _restaurantNearbyRating.value = firstRestaurantNearby.rating.toString()
        _restaurantNearbyRatingCount.value = "(${firstRestaurantNearby.numberReviews})"
        _restaurantNearbyDistance.value = firstRestaurantNearby.distance

        _attractionNearbyPhoto.value =
            firstAttractionNearby.photoUrlTemplate.replace("{width}", "500")
                .replace("{height}", "300")
        _attractionNearbyName.value = firstAttractionNearby.title
        _attractionNearbyInfo.value = firstAttractionNearby.primaryInfo
        _attractionNearbyRating.value = firstAttractionNearby.rating.toString()
        _attractionNearbyRatingCount.value = "(${firstAttractionNearby.numberReviews})"
        _attractionNearbyDistance.value = firstAttractionNearby.distance
    }

    private fun List<String>.getAmenityByTitle(titles: List<String>): String {
        var amenityToReturn = ""
        this.forEach { amenity ->
            titles.forEach { title ->
                if (amenity.lowercase(Locale.ROOT).contains(title)) amenityToReturn = amenity
            }
        }
        return amenityToReturn
    }


    companion object {

        @JvmStatic
        @BindingAdapter("detailsHotelImage")
        fun loadHotelImage(view: ShapeableImageView, source: String?) {
            view.shapeAppearanceModel =
                view.shapeAppearanceModel.toBuilder().setAllCornerSizes(50F).build()
            if (source.isNullOrEmpty()) {
                view.load(R.drawable.sample_hotel_item_img_500x300)
            } else view.load(source)
        }

        @JvmStatic
        @BindingAdapter("detailsRestaurantNearbyImage")
        fun loadRestaurantNearbyImage(view: ShapeableImageView, source: String?) {
            view.shapeAppearanceModel =
                view.shapeAppearanceModel.toBuilder().setAllCornerSizes(50F).build()
            if (source.isNullOrEmpty()) {
                view.load(R.drawable.sample_hotel_item_img_500x300)
            } else view.load(source)
        }

        @JvmStatic
        @BindingAdapter("detailsAttractionNearbyImage")
        fun loadAttractionNearbyImage(view: ShapeableImageView, source: String?) {
            view.shapeAppearanceModel =
                view.shapeAppearanceModel.toBuilder().setAllCornerSizes(50F).build()
            if (source.isNullOrEmpty()) {
                view.load(R.drawable.sample_hotel_item_img_500x300)
            } else view.load(source)
        }
    }
}