package com.inlay.hotelroomservice.presentation.viewmodels.hotels

import androidx.lifecycle.viewModelScope
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCase
import com.inlay.hotelroomservice.presentation.models.CombinedHotelsData
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class AppHotelsViewModel(
    private val repositoryUseCase: RepositoryUseCase,
    private val dateFormat: SimpleDateFormat
) : HotelsViewModel() {
    private val _isOnline = MutableStateFlow(false)
    override val isOnline = _isOnline

    private val _selectedDates = MutableStateFlow(DatesModel("", ""))
    override val selectedDates = _selectedDates

    private val _selectedCurrency = MutableStateFlow("USD")
    override val selectedCurrency = _selectedCurrency

    private val _hotelDetailsSearchModel =
        MutableStateFlow(HotelDetailsSearchModel("", _selectedDates.value, _selectedCurrency.value))
    override val hotelDetailsSearchModel = _hotelDetailsSearchModel

    private val _hotelsDataList = MutableStateFlow<List<HotelsItemUiModel>>(listOf())
    override val hotelsDataList = _hotelsDataList

    private val _selectedHotelsDataList = MutableStateFlow(listOf<HotelsItemUiModel>())
    override val selectedHotelsDataList = _selectedHotelsDataList

//    private val _combinedHotelsData =
//        MutableStateFlow(
//            CombinedHotelsData(
//                _hotelsDataList.value,
//                _selectedDates.value,
//                _selectedCurrency.value
//            )
//        )
//    override val combinedHotelsData = _combinedHotelsData

    override fun initialize(isOnline: Boolean) {
        viewModelScope.launch {
            _isOnline.value = isOnline
            val dummyDates = getDummyDates()
            _selectedDates.value = dummyDates
            getHotelsRepo(
                _isOnline.value,
                _selectedDates.value.checkInDate,
                _selectedDates.value.checkOutDate,
                _selectedCurrency.value
            )
        }
    }


    override fun getHotelsRepo(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _hotelsDataList.value = repositoryUseCase.getHotelsRepo(
                isOnline,
                geoId,
                checkInDate,
                checkOutDate,
                currencyCode
            )

//            _combinedHotelsData.value = CombinedHotelsData(
//                _hotelsDataList.value,
//                _selectedDates.value,
//                _selectedCurrency.value
//            )
        }
    }

    override fun setSelectedData(dates: DatesModel, currency: String) {
        _selectedDates.value = dates
        _selectedCurrency.value = currency

//        _combinedHotelsData.value = CombinedHotelsData(
//            _hotelsDataList.value,
//            _selectedDates.value,
//            _selectedCurrency.value
//        )
    }

    override fun updateHotelDetailsSearchModel(id: String) {
        _hotelDetailsSearchModel.value =
            HotelDetailsSearchModel(id, _selectedDates.value, _selectedCurrency.value)
    }

    private fun getDummyDates(): DatesModel {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val checkInDate = dateFormat.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val checkOutDate = dateFormat.format(calendar.time)

        return DatesModel(checkInDate, checkOutDate)
    }
}