package com.inlay.hotelroomservice.presentation.viewmodels.hotels

import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.flow.StateFlow

abstract class HotelsViewModel : ViewModel() {
    abstract val hotelsDataList: StateFlow<List<HotelsItemUiModel>>
    abstract val selectedHotelsDataList: StateFlow<List<HotelsItemUiModel>>

    abstract fun getHotelsRepo(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String = "USD"
    )
}