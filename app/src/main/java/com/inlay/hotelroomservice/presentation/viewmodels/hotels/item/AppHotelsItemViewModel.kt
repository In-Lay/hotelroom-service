package com.inlay.hotelroomservice.presentation.viewmodels.hotels.item

import androidx.databinding.BindingAdapter
import androidx.lifecycle.asLiveData
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsDatesAndCurrencyModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.annotations.VisibleForTesting

class AppHotelsItemViewModel : HotelsItemViewModel() {
    private val _hotelItemData: MutableStateFlow<HotelsItemUiModel?> = MutableStateFlow(null)
    override val hotelItemData = _hotelItemData.asLiveData()

    private val _hotelDetailsSearchModel: MutableStateFlow<HotelDetailsSearchModel?> =
        MutableStateFlow(null)
    override val hotelDetailsSearchModel = _hotelDetailsSearchModel


    private val _hotelName = MutableStateFlow("")
    override val hotelName = _hotelName.asLiveData()

    private lateinit var goToDetails: (HotelDetailsSearchModel) -> Unit

    private lateinit var addOrRemoveStay: (HotelsItemUiModel) -> Unit

    override fun goToDetails() {
        _hotelDetailsSearchModel.value?.let {
            goToDetails(it)
        }
    }

    override fun addRemoveStay() {
        _hotelItemData.value?.let { addOrRemoveStay(it) }
    }

    override fun initializeData(
        hotelsUiModel: HotelsItemUiModel,
        hotelsDatesAndCurrencyModel: HotelsDatesAndCurrencyModel,
        openDetails: (HotelDetailsSearchModel) -> Unit,
        addOrRemoveStay: (HotelsItemUiModel) -> Unit
    ) {
        _hotelItemData.value = hotelsUiModel

        _hotelDetailsSearchModel.value = HotelDetailsSearchModel(
            hotelsUiModel.id,
            hotelsDatesAndCurrencyModel.datesModel,
            hotelsDatesAndCurrencyModel.currency
        )

        _hotelName.value = if (hotelsUiModel.title[0].isDigit()) hotelsUiModel.title.removeRange(
            0, hotelsUiModel.title.indexOf(' ') + 1
        ) else hotelsUiModel.title

        goToDetails = openDetails

        this.addOrRemoveStay = addOrRemoveStay
    }

    companion object {
        @JvmStatic
        @BindingAdapter("hotelsItemImageSource")
        fun loadImage(view: ShapeableImageView, imageUrl: String?) {
            view.shapeAppearanceModel =
                view.shapeAppearanceModel.toBuilder().setAllCorners(CornerFamily.ROUNDED, 50F)
                    .build()
            if (imageUrl.isNullOrEmpty()) {
                view.load(R.drawable.sample_hotel_item_img_500x300)
            } else {
                view.load(imageUrl)
            }
        }
    }
}