package com.inlay.hotelroomservice.presentation.viewmodels.search.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel

abstract class SearchLocationsItemViewModel : ViewModel() {
    abstract val searchLocationsData: LiveData<SearchLocationsUiModel?>

    abstract fun initialize(
        searchLocationsUiModel: SearchLocationsUiModel,
        selectItem: (SearchLocationsUiModel) -> Unit
    )

    abstract fun selectCurrentItem()
}