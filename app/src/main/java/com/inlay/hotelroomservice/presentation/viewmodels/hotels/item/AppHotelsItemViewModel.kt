package com.inlay.hotelroomservice.presentation.viewmodels.hotels.item

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.RoundedCornersTransformation
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow

class AppHotelsItemViewModel : HotelsItemViewModel() {
    private val _hotelId = MutableStateFlow("")
    override val hotelId = _hotelId

    private val _hotelName = MutableStateFlow("")
    override val hotelName = _hotelName.asLiveData()

    private val _hotelInfo = MutableStateFlow("")
    override val hotelInfo = _hotelInfo.asLiveData()

    private val _rating = MutableStateFlow("")
    override val rating = _rating.asLiveData()

    private val _price = MutableStateFlow("")
    override val price = _price.asLiveData()

    private val _imageUrl = MutableStateFlow("")
    override val imageUrl = _imageUrl.asLiveData()

    private lateinit var goToDetailsLambda: (String) -> Unit

    override fun goToDetails() {
        goToDetailsLambda(_hotelId.value)
    }

    override fun initializeData(hotelsUiModel: HotelsItemUiModel, openDetails: (String) -> Unit) {
        _hotelId.value = hotelsUiModel.id
        _hotelName.value = hotelsUiModel.title
        _hotelInfo.value = hotelsUiModel.hotelInfo
        _rating.value = hotelsUiModel.rating
        _price.value = hotelsUiModel.price
        val formattedUrl = hotelsUiModel.photosUrls?.get(0)?.replace("{width}", "500")
            ?.replace("{height}", "300") ?: ""
        _imageUrl.value = formattedUrl
        goToDetailsLambda = openDetails
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageSource")
        fun loadImage(view: ImageView, imageUrl: String) {
            view.load(imageUrl) {
                transformations(RoundedCornersTransformation(50F))
            }
        }
    }
}