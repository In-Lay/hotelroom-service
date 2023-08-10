package com.inlay.hotelroomservice.presentation.viewmodels.hotels

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
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
import kotlinx.coroutines.flow.collectLatest
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

    private val _language = MutableStateFlow("en")
    override val language = _language

    private val _darkModeState = MutableStateFlow(AppCompatDelegate.MODE_NIGHT_NO)
    override val darkModeState = _darkModeState

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
        viewModelScope.launch {
            repositoryUseCase.getLanguage().collect {
                Log.d("SettingsLog", "AppHotelsViewModel: init: language: $it")
                _language.value = it
            }
        }

        viewModelScope.launch {
            repositoryUseCase.getNightModeState().collect {
                _darkModeState.value = it
            }
        }
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

            getStaysRepo(_isOnline.value, isUserLogged())
        }
    }

    override fun changeLanguage(languageCode: String) {
        _language.value = languageCode
        viewModelScope.launch {
            repositoryUseCase.saveLanguage(languageCode)
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

    override fun getStaysRepo(isOnline: Boolean, isLogged: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUseCase.getStaysRepo(isOnline, isLogged).collectLatest { list ->
                _selectedHotelsDataList.value = list.mapNotNull { it }
            }
        }
    }

    override fun addStay(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean) {
        val dataList = _selectedHotelsDataList.value.toMutableList()
        dataList.add(hotelsItem)
        _selectedHotelsDataList.value = dataList
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUseCase.addStayRepo(hotelsItem, isOnline, isLogged)
        }
    }

    override fun removeStay(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean) {
        val dataList = _selectedHotelsDataList.value.toMutableList()
        dataList.remove(hotelsItem)
        _selectedHotelsDataList.value = dataList
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUseCase.removeStayRepo(hotelsItem, isOnline, isLogged)
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

    private fun isUserLogged(): Boolean = _user.value != null

}