package com.inlay.hotelroomservice.presentation.viewmodels.details

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.domain.usecase.details.GetHotelDetails
import com.inlay.hotelroomservice.presentation.models.AppResult
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class AppDetailsViewModel(private val getHotelDetails: GetHotelDetails) : DetailsViewModel() {

    private val _hotelDetailsData: MutableStateFlow<HotelDetailsUiModel?> = MutableStateFlow(
        HotelDetailsUiModel(
            "",
            "0",
            "0",
            "",
            "",
            "",
            listOf(),
            "",
            listOf(),
            listOf(),
            "",
            listOf(),
            listOf(),
            0.0,
            0.0
        )
    )
    override val hotelDetailsData: LiveData<HotelDetailsUiModel?> = _hotelDetailsData.asLiveData()

    private val _errorMessage = MutableStateFlow("")
    override val errorMessage = _errorMessage

    private val _hotelImagesList = MutableStateFlow(listOf<String>())
    override val hotelImagesList = _hotelImagesList


    private val _hotelImage = MutableStateFlow("")
    override val hotelImage = _hotelImage.asLiveData()

    private val _hotelPrice = MutableStateFlow("")
    override val hotelPrice = _hotelPrice.asLiveData()


    private val _chipParkingText = MutableStateFlow("")
    override val chipParkingText = _chipParkingText.asLiveData()

    private val _chipInternetText = MutableStateFlow("")
    override val chipInternetText = _chipInternetText.asLiveData()

    private val _chipGymText = MutableStateFlow("")
    override val chipGymText = _chipGymText.asLiveData()

    private val _chipFoodText = MutableStateFlow("")
    override val chipFoodText = _chipFoodText.asLiveData()


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


    private val _attractionNearbyPhoto = MutableStateFlow("")
    override val attractionNearbyPhoto = _attractionNearbyPhoto.asLiveData()

    private lateinit var openImageDialogLambda: () -> Unit
    private lateinit var viewAllRestaurantsLambda: () -> Unit
    private lateinit var viewAllAttractionsLambda: () -> Unit
    private lateinit var openLinkInBrowserLambda: (String) -> Unit

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

    override fun initializeData(
        openImageDialog: () -> Unit,
        viewAllRestaurants: () -> Unit,
        viewAllAttractions: () -> Unit,
        openLinkInBrowser: (String) -> Unit,
        hotelDetailsSearchModel: HotelDetailsSearchModel
    ) {
        openImageDialogLambda = openImageDialog
        viewAllRestaurantsLambda = viewAllRestaurants
        viewAllAttractionsLambda = viewAllAttractions
        openLinkInBrowserLambda = openLinkInBrowser

        getDetailsData(hotelDetailsSearchModel)
    }

    private fun getDetailsData(
        hotelDetailsSearchModel: HotelDetailsSearchModel
    ) {
        viewModelScope.launch {
            val data = getHotelDetails(
                hotelDetailsSearchModel.id,
                hotelDetailsSearchModel.dates.checkInDate,
                hotelDetailsSearchModel.dates.checkOutDate,
                hotelDetailsSearchModel.currency
            )
            when (data) {
                is AppResult.Success -> {
                    _hotelDetailsData.value = data.data

                    assignDetailsDataToViews()
                }

                is AppResult.Error -> {
                    _errorMessage.value = data.error.toString()
                }
            }
        }
    }

    private fun assignDetailsDataToViews() {
        _hotelImagesList.value = _hotelDetailsData.value?.photos.orEmpty()

        _hotelImage.value =
            if (_hotelDetailsData.value?.photos.isNullOrEmpty()) "" else _hotelDetailsData.value?.photos?.get(
                0
            ).orEmpty().makeImageLink()


        _chipParkingText.value =
            _hotelDetailsData.value?.aboutAmenities?.getAmenityByTitle(AmenityTitles.PARKING.titles)
                .orEmpty()
        _chipInternetText.value =
            _hotelDetailsData.value?.aboutAmenities?.getAmenityByTitle(AmenityTitles.INTERNET.titles)
                .orEmpty()
        _chipGymText.value =
            _hotelDetailsData.value?.aboutAmenities?.getAmenityByTitle(AmenityTitles.FITNESS.titles)
                .orEmpty()
        _chipFoodText.value =
            _hotelDetailsData.value?.aboutAmenities?.getAmenityByTitle(AmenityTitles.BAR.titles)
                .orEmpty()



        _hotelPrice.value =
            if (_hotelDetailsData.value?.displayPrice.isNullOrEmpty()) "Visit the website by Link below" else _hotelDetailsData.value?.displayPrice.toString()

        _hotelProvider.value = "by ${_hotelDetailsData.value?.providerName}"


        _hotelLink.value =
            if (_hotelDetailsData.value?.aboutLinks.isNullOrEmpty()) "" else _hotelDetailsData.value?.aboutLinks?.get(
                0
            ).orEmpty()

        _hotelAbout.value =
            if (_hotelDetailsData.value?.aboutTitle.isNullOrEmpty()) _hotelDetailsData.value?.rankingDetails?.replace(
                "#", "№"
            )?.replace(Regex("<a>|</a>"), "")
                .orEmpty() else _hotelDetailsData.value?.aboutTitle.orEmpty()

        _restaurantsNearby.value = _hotelDetailsData.value?.restaurantsNearby.orEmpty()
        _attractionsNearby.value = _hotelDetailsData.value?.attractionsNearby.orEmpty()

        val firstRestaurantNearby = _hotelDetailsData.value?.restaurantsNearby?.get(0)
        val firstAttractionNearby = _hotelDetailsData.value?.attractionsNearby?.get(0)

        _restaurantNearbyPhoto.value =
            firstRestaurantNearby?.photoUrlTemplate.orEmpty().makeImageLink()

        _attractionNearbyPhoto.value =
            firstAttractionNearby?.photoUrlTemplate.orEmpty().makeImageLink()
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

    private fun String.makeImageLink(width: String = "500", height: String = "300") =
        this.replace("{width}", width).replace("{height}", height)

    companion object {

        @JvmStatic
        @BindingAdapter("detailsImages")
        fun loadHotelImage(view: ShapeableImageView, source: String?) {
            view.shapeAppearanceModel =
                view.shapeAppearanceModel.toBuilder().setAllCornerSizes(50F).build()
            if (source.isNullOrEmpty()) {
                view.load(R.drawable.sample_hotel_item_img_500x300) {
                    allowHardware(false)
                }
            } else view.load(source) {
                allowHardware(false)
            }
        }
    }
}