package com.inlay.hotelroomservice.presentation.viewmodels.hotels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsDatesAndCurrencyModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.flow.StateFlow

abstract class HotelsViewModel : ViewModel() {
    abstract val user: StateFlow<FirebaseUser?>

    abstract val isOnline: StateFlow<Boolean>

    abstract val hotelsDatesAndCurrencyModel: StateFlow<HotelsDatesAndCurrencyModel?>

    abstract val hotelDetailsSearchModel: StateFlow<HotelDetailsSearchModel?>

    abstract val hotelsDataList: StateFlow<List<HotelsItemUiModel>>
    abstract val selectedHotelsDataList: StateFlow<List<HotelsItemUiModel>>

    abstract fun initialize(isOnline: Boolean)

    abstract fun getHotelsRepo(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String = "USD"
    )

    abstract fun getStaysRepo(isOnline: Boolean, isLogged: Boolean)

    abstract fun addStay(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean)

    abstract fun removeStay(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean)
}