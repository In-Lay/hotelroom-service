package com.inlay.hotelroomservice.presentation.viewmodels.search

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCase
import com.inlay.hotelroomservice.presentation.models.SearchDataUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class AppSearchViewModel(private val repositoryUseCase: RepositoryUseCase) : SearchViewModel() {

    private val _isOnline = MutableStateFlow(false)
    override val isOnline = _isOnline

    private val _searchUserInput = MutableStateFlow("")
    override val searchUserInput = _searchUserInput.asLiveData()

    private val _supportText = MutableStateFlow("")
    override val supportText = _supportText.asLiveData()

    private val _searchLocationsData = MutableStateFlow<List<SearchLocationsUiModel>>(listOf())
    override val searchLocationsData = _searchLocationsData

    private val _selectedItemGeoId = MutableStateFlow("")
    override val selectedItemGeoId = _selectedItemGeoId

    private val _dates = MutableStateFlow(DatesModel("", ""))
    override val dates = _dates.asLiveData()

    private val _currencyCode = MutableStateFlow("")
    override val currencyCode = _currencyCode.asLiveData()

    private val _searchData = MutableStateFlow(SearchDataUiModel("", "", "", ""))
    override val searchData = _searchData

    private lateinit var openDatePickerLambda: () -> DatesModel
    private lateinit var searchHotelsLambda: (SearchDataUiModel) -> Unit

    override fun init(
        onlineStatus: Boolean,
        openDatePicker: () -> DatesModel,
        searchHotels: (SearchDataUiModel) -> Unit
    ) {
        _isOnline.value = onlineStatus
        openDatePickerLambda = openDatePicker
        searchHotelsLambda = searchHotels
    }

    override fun setCurrentItemGeoId(geoId: String) {
        _selectedItemGeoId.value = geoId
    }

    override fun getSearchLocations() {
        if (_searchUserInput.value.isEmpty()) {
            _supportText.value = "Enter location"
        } else {
            viewModelScope.launch {
                _searchLocationsData.value = repositoryUseCase.getSearchLocationRepo(
                    _searchUserInput.value.lowercase(
                        Locale.ROOT
                    )
                )
            }
        }
    }

    override fun openDatePicker() {
        _dates.value = openDatePickerLambda()
    }

    override fun searchHotels() {
        _searchData.value = SearchDataUiModel(
            _selectedItemGeoId.value,
            _dates.value.checkInDate,
            _dates.value.checkOutDate,
            _currencyCode.value
        )
        if (searchData.value.checkInDate.isEmpty() || searchData.value.checkOutDate.isEmpty()) {
            _supportText.value = "Pick dates"
        } else {
            searchHotelsLambda(_searchData.value)
        }
    }
}