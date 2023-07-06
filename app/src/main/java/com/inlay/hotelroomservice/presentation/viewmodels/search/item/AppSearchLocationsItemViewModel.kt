package com.inlay.hotelroomservice.presentation.viewmodels.search.item

import androidx.lifecycle.asLiveData
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.MutableStateFlow

class AppSearchLocationsItemViewModel : SearchLocationsItemViewModel() {
    private val _geoId = MutableStateFlow("")
    override val geoId = _geoId

    private val _title = MutableStateFlow("")
    override val title = _title.asLiveData()

    private val _secondaryText = MutableStateFlow("")
    override val secondaryText = _secondaryText.asLiveData()

    private val _imageUrl = MutableStateFlow("")
    override val imageUrl = _imageUrl.asLiveData()

    private lateinit var selectCurrentItemLambda: (String) -> Unit

    override fun initialize(
        searchLocationsUiModel: SearchLocationsUiModel,
        selectItem: (String) -> Unit
    ) {
        _geoId.value = searchLocationsUiModel.geoId ?: ""
        _title.value = searchLocationsUiModel.title
        _secondaryText.value = searchLocationsUiModel.secondaryText
        _imageUrl.value = searchLocationsUiModel.image?.urlTemplate ?: ""
        selectCurrentItemLambda = selectItem
    }

    override fun selectCurrentItem() {
        selectCurrentItemLambda(_geoId.value)
    }
}