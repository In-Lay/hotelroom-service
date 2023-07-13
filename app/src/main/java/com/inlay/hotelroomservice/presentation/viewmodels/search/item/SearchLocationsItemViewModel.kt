package com.inlay.hotelroomservice.presentation.viewmodels.search.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.StateFlow

abstract class SearchLocationsItemViewModel : ViewModel() {
    abstract val geoId: StateFlow<String>
    abstract val title: LiveData<String>
    abstract val secondaryText: LiveData<String>
    abstract val imageUrl: LiveData<String>

    abstract val searchLocationsUiModel: StateFlow<SearchLocationsUiModel>

    abstract fun initialize(
        searchLocationsUiModel: SearchLocationsUiModel,
        selectItem: (SearchLocationsUiModel) -> Unit
    )

    abstract fun selectCurrentItem()
}