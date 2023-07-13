package com.inlay.hotelroomservice.presentation.viewmodels.search.item

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.asLiveData
import coil.load
import coil.transform.RoundedCornersTransformation
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsImageUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppSearchLocationsItemViewModel : SearchLocationsItemViewModel() {
    private val _geoId = MutableStateFlow("")
    override val geoId = _geoId

    private val _title = MutableStateFlow("")
    override val title = _title.asLiveData()

    private val _secondaryText = MutableStateFlow("")
    override val secondaryText = _secondaryText.asLiveData()

    private val _imageUrl = MutableStateFlow("")
    override val imageUrl = _imageUrl.asLiveData()

    private val _searchLocationsUiModel = MutableStateFlow(
        SearchLocationsUiModel(
            "",
            "",
            "",
            SearchLocationsImageUiModel("", "")
        )
    )
    override val searchLocationsUiModel = _searchLocationsUiModel

    private lateinit var selectCurrentItemLambda: (SearchLocationsUiModel) -> Unit

    override fun initialize(
        searchLocationsUiModel: SearchLocationsUiModel, selectItem: (SearchLocationsUiModel) -> Unit
    ) {
        _searchLocationsUiModel.value = searchLocationsUiModel
        _geoId.value = searchLocationsUiModel.geoId ?: ""
        _title.value = searchLocationsUiModel.title
        _secondaryText.value = searchLocationsUiModel.secondaryText
        _imageUrl.value = searchLocationsUiModel.image?.urlTemplate ?: ""
        selectCurrentItemLambda = selectItem
    }

    override fun selectCurrentItem() {
        selectCurrentItemLambda(_searchLocationsUiModel.value)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imgSource")
        fun loadImage(view: ImageView, imageUrl: String?) {
            if (imageUrl.isNullOrEmpty()) {
                view.load(R.drawable.sample_locations_image) {
                    transformations(RoundedCornersTransformation(50F))
                }
            } else {
                view.load(imageUrl) {
                    transformations(RoundedCornersTransformation(50F))
                }
            }
        }
    }
}