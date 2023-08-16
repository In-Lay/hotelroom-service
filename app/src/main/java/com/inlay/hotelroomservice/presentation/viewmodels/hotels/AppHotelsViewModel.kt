package com.inlay.hotelroomservice.presentation.viewmodels.hotels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.inlay.hotelroomservice.domain.usecase.datastore.nightmode.GetNightMode
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.GetNotificationsState
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.SaveNotificationsState
import com.inlay.hotelroomservice.domain.usecase.hotels.GetHotelsRepo
import com.inlay.hotelroomservice.domain.usecase.sharedpreferences.GetLanguagePreferences
import com.inlay.hotelroomservice.domain.usecase.sharedpreferences.SaveLanguagePreferences
import com.inlay.hotelroomservice.domain.usecase.stays.add.AddStays
import com.inlay.hotelroomservice.domain.usecase.stays.get.GetStay
import com.inlay.hotelroomservice.domain.usecase.stays.remove.RemoveStay
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
    private val getHotelsRepoUseCase: GetHotelsRepo,
    private val getStay: GetStay,
    private val addStays: AddStays,
    private val removeStayUseCase: RemoveStay,
    private val getLanguagePreferences: GetLanguagePreferences,
    private val saveLanguagePreferences: SaveLanguagePreferences,
    private val getNightMode: GetNightMode,
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

    private val _notificationsAvailability = MutableStateFlow(false)
    override val notificationsAvailability = _notificationsAvailability

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
            _language.value = getLanguagePreferences.getLanguage().toString()
        }

        viewModelScope.launch {
            getNightMode().collect {
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
            saveLanguagePreferences.saveLanguage(languageCode)
        }
    }

    override fun changeNotificationsAvailability(notificationsState: Boolean) {
        _notificationsAvailability.value = notificationsState
    }

    override fun getHotelsRepo(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _hotelsDataList.value = getHotelsRepoUseCase(
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
            getStay(isOnline, isLogged).collectLatest { list ->
                _selectedHotelsDataList.value = list.mapNotNull { it }
            }
        }
    }

    override fun addStay(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean) {
        val dataList = _selectedHotelsDataList.value.toMutableList()
        dataList.add(hotelsItem)
        _selectedHotelsDataList.value = dataList
        viewModelScope.launch(Dispatchers.IO) {
            addStays(hotelsItem, isOnline, isLogged)
        }
    }

    override fun removeStay(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean) {
        val dataList = _selectedHotelsDataList.value.toMutableList()
        dataList.remove(hotelsItem)
        _selectedHotelsDataList.value = dataList
        viewModelScope.launch(Dispatchers.IO) {
            removeStayUseCase(hotelsItem, isOnline, isLogged)
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