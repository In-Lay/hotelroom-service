package com.inlay.hotelroomservice.presentation.viewmodels.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.SearchDataUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.StateFlow

abstract class SearchViewModel : ViewModel() {
    abstract val isOnline: StateFlow<Boolean>

    abstract val searchUserInput: LiveData<String>

    abstract val supportText: LiveData<String>

    abstract val searchLocationsData: StateFlow<List<SearchLocationsUiModel>>

    abstract val selectedItemGeoId: StateFlow<String>

    abstract val dates: LiveData<DatesModel>
    abstract val currencyCode: LiveData<String>

    abstract val searchData: StateFlow<SearchDataUiModel>

    abstract fun init(
        onlineStatus: Boolean,
        openDatePicker: () -> DatesModel,
        searchHotels: (SearchDataUiModel) -> Unit
    )

    abstract fun setCurrentItemGeoId(geoId: String)

    abstract fun getSearchLocations()

    abstract fun searchHotels()

    abstract fun openDatePicker()
}