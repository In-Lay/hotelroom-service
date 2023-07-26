package com.inlay.hotelroomservice.presentation.viewmodels.search.item

import androidx.databinding.BindingAdapter
import androidx.lifecycle.asLiveData
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.MutableStateFlow

class AppSearchLocationsItemViewModel : SearchLocationsItemViewModel() {
    private val _searchLocationsData: MutableStateFlow<SearchLocationsUiModel?> =
        MutableStateFlow(null)
    override val searchLocationsData = _searchLocationsData.asLiveData()

    private lateinit var selectCurrentItemLambda: (SearchLocationsUiModel) -> Unit

    override fun initialize(
        searchLocationsUiModel: SearchLocationsUiModel, selectItem: (SearchLocationsUiModel) -> Unit
    ) {
        _searchLocationsData.value = searchLocationsUiModel

        selectCurrentItemLambda = selectItem
    }

    override fun selectCurrentItem() {
        _searchLocationsData.value?.let { selectCurrentItemLambda(it) }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("locationsItemImageSource")
        fun loadImage(view: ShapeableImageView, imageUrl: String?) {
            view.shapeAppearanceModel =
                view.shapeAppearanceModel.toBuilder().setAllCorners(CornerFamily.ROUNDED, 50F)
                    .build()
            if (imageUrl.isNullOrEmpty()) {
                view.load(R.drawable.sample_locations_image)
            } else {
                view.load(imageUrl)
            }
        }
    }
}