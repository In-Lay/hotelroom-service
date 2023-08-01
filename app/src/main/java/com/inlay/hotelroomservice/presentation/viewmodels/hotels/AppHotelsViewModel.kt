package com.inlay.hotelroomservice.presentation.viewmodels.hotels

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCase
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsDatesAndCurrencyModel
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
    private val _user: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    override val user = _user

    private val _isOnline = MutableStateFlow(false)
    override val isOnline = _isOnline

    private val _hotelsDatesAndCurrencyModel: MutableStateFlow<HotelsDatesAndCurrencyModel?> =
        MutableStateFlow(null)
    override val hotelsDatesAndCurrencyModel = _hotelsDatesAndCurrencyModel

    private val _hotelDetailsSearchModel: MutableStateFlow<HotelDetailsSearchModel?> =
        MutableStateFlow(null)
    override val hotelDetailsSearchModel = _hotelDetailsSearchModel

    private val _hotelsDataList = MutableStateFlow<List<HotelsItemUiModel>>(listOf())
    override val hotelsDataList = _hotelsDataList

    private val _selectedHotelsDataList = MutableStateFlow(listOf<HotelsItemUiModel>())
    override val selectedHotelsDataList = _selectedHotelsDataList

    init {
        _user.value = Firebase.auth.currentUser
    }

    override fun initialize(isOnline: Boolean) {
        viewModelScope.launch {
            _isOnline.value = isOnline
            val dummyDates = getDummyDates()
            val hotelsDatesAndCurrency = HotelsDatesAndCurrencyModel(dummyDates, "USD")
            _hotelsDatesAndCurrencyModel.value = hotelsDatesAndCurrency
            getHotelsRepo(
                _isOnline.value,
                hotelsDatesAndCurrency.datesModel.checkInDate,
                hotelsDatesAndCurrency.datesModel.checkOutDate,
                hotelsDatesAndCurrency.currency
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
            val dates = DatesModel(checkInDate, checkOutDate)
            _hotelsDatesAndCurrencyModel.value = HotelsDatesAndCurrencyModel(dates, currencyCode)
        }
    }

    override fun getStaysRepo(isOnline: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedHotelsDataList.value = repositoryUseCase.getStaysRepo(isOnline)
        }
    }

    override fun addStay(hotelsItem: HotelsItemUiModel) {
        val dataList = _selectedHotelsDataList.value.toMutableList()
        dataList.add(hotelsItem)
        _selectedHotelsDataList.value = dataList
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUseCase.addStayRepo(hotelsItem)
        }
    }

    override fun removeStay(hotelsItem: HotelsItemUiModel) {
        val dataList = _selectedHotelsDataList.value.toMutableList()
        dataList.remove(hotelsItem)
        _selectedHotelsDataList.value = dataList
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUseCase.removeStayRepo(hotelsItem)
        }
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