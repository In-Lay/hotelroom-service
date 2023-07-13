package com.inlay.hotelroomservice.presentation.viewmodels.search

import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCase
import com.inlay.hotelroomservice.presentation.models.SearchDataUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsImageUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _selectedItemModel = MutableStateFlow(
        SearchLocationsUiModel(
            "",
            "",
            "",
            SearchLocationsImageUiModel("", "")
        )
    )
    override val selectedItemModel = _selectedItemModel

    private val _selectedItemImage = MutableStateFlow("")
    override val selectedItemImage = _selectedItemImage.asLiveData()

    private val _selectedItemTitle = MutableStateFlow("")
    override val selectedItemTitle = _selectedItemTitle.asLiveData()

    private val _selectedItemInfo = MutableStateFlow("")
    override val selectedItemInfo = _selectedItemInfo.asLiveData()

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
        Log.d("SearchTag", "Init")
        _isOnline.value = onlineStatus
        Log.d("SearchTag", "Init: _isOnline.value: ${_isOnline.value}")
        openDatePickerLambda = openDatePicker
        searchHotelsLambda = searchHotels
    }

    override fun setCurrentItemModel(model: SearchLocationsUiModel) {
        Log.d("SearchTag", "setCurrentItemModel: model: $model")
        _selectedItemModel.value = model
        _selectedItemImage.value = model.image?.urlTemplate.orEmpty()
        _selectedItemTitle.value = model.title
        Log.d(
            "SearchTag",
            "setCurrentItemModel: _selectedItemTitle.value: ${_selectedItemTitle.value}"
        )
        _selectedItemInfo.value = model.secondaryText
    }

    override fun getSearchLocations(searchInput: String) {
        _searchUserInput.value = searchInput
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
        Log.d("SearchTag", "viewModel: openDatePicker")
        _dates.value = openDatePickerLambda()
        Log.d("SearchTag", "viewModel: _dates.value: ${_dates.value}")
    }

    override fun searchHotels() {
        Log.d("SearchTag", "viewModel: searchHotels clicked")
        _searchData.value = SearchDataUiModel(
            _selectedItemModel.value.geoId,
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

    companion object {
        @JvmStatic
        @BindingAdapter("imgSource")
        fun loadImage(view: ImageView, imgSource: String?) {
            if (imgSource.isNullOrEmpty()) {
                view.load(R.drawable.sample_locations_image) {
                    transformations(RoundedCornersTransformation(50F))
                }
            } else {
                view.load(imgSource) {
                    transformations(RoundedCornersTransformation(50F))
                }
            }
        }
    }
}