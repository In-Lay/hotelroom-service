package com.inlay.hotelroomservice.presentation.viewmodels.hotels.item

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.RoundedCornersTransformation
import com.inlay.hotelroomservice.R
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

    private val _ratingCount = MutableStateFlow("")
    override val ratingCount = _ratingCount.asLiveData()

    private val _price = MutableStateFlow("")
    override val price = _price.asLiveData()

    private val _imageUrl = MutableStateFlow("")
    override val imageUrl = _imageUrl.asLiveData()

    private lateinit var goToDetailsLambda: (String) -> Unit

    override fun goToDetails() {
        goToDetailsLambda(_hotelId.value)
    }

    override fun addToStay() {
        TODO("Not yet implemented")
    }

    override fun initializeData(hotelsUiModel: HotelsItemUiModel, openDetails: (String) -> Unit) {
        _hotelId.value = hotelsUiModel.id

        _hotelName.value = if (hotelsUiModel.title[0].isDigit()) hotelsUiModel.title.removeRange(
            0, hotelsUiModel.title.indexOf(' ') + 1
        ) else hotelsUiModel.title

        _hotelInfo.value = hotelsUiModel.hotelInfo
        _rating.value = hotelsUiModel.rating.rating.toString()
        _ratingCount.value = "(${hotelsUiModel.rating.count})"
        _price.value = hotelsUiModel.price
        _imageUrl.value = hotelsUiModel.photosUrls[0]
        goToDetailsLambda = openDetails
    }

    companion object {
        @JvmStatic
        @BindingAdapter("hotelsItemImageSource")
        fun loadImage(view: ImageView, imageUrl: String?) {
            if (imageUrl.isNullOrEmpty()) {
                view.load(R.drawable.sample_hotel_item_img_500x300) {
                    transformations(
                        RoundedCornersTransformation(50F)
                    )
                }
            } else {
                view.load(imageUrl) { transformations(RoundedCornersTransformation(50F)) }
            }
        }
    }
}