package com.inlay.hotelroomservice.presentation.viewmodels.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.SearchDataUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.annotations.VisibleForTesting

abstract class SearchViewModel : ViewModel() {
    abstract val isOnline: StateFlow<Boolean>

    abstract val searchUserInput: LiveData<String>

    abstract val supportText: LiveData<String>

    abstract val searchLocationsData: StateFlow<List<SearchLocationsUiModel>>

    abstract val selectedItemModel: StateFlow<SearchLocationsUiModel>

    abstract val dates: LiveData<DatesModel>
    abstract val currencyCode: LiveData<String>

    abstract val searchData: StateFlow<SearchDataUiModel>

    abstract fun init(
        onlineStatus: Boolean,
        openDatePicker: () -> Unit,
        searchHotels: (SearchDataUiModel) -> Unit
    )

    abstract fun setCurrentItemModel(model: SearchLocationsUiModel)

    abstract fun getSearchLocations(searchInput: String)

    abstract fun searchHotels()

    abstract fun openDatePicker()

    abstract fun setDates(dates: DatesModel)

    @VisibleForTesting
    abstract fun setSearchData(
        selectedItemModel: SearchLocationsUiModel,
        dates: DatesModel,
        currencyCode: String
    )
}