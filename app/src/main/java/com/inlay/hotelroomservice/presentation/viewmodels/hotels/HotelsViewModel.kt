package com.inlay.hotelroomservice.presentation.viewmodels.hotels

import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.CombinedHotelsData
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.flow.StateFlow

abstract class HotelsViewModel : ViewModel() {
    abstract val isOnline: StateFlow<Boolean>
    abstract val selectedDates: StateFlow<DatesModel>
    abstract val selectedCurrency: StateFlow<String>

//    abstract val combinedHotelsData: StateFlow<CombinedHotelsData<HotelsItemUiModel>>

    abstract val hotelDetailsSearchModel: StateFlow<HotelDetailsSearchModel>

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

    abstract fun setSelectedData(dates: DatesModel, currency: String = "USD")

    abstract fun updateHotelDetailsSearchModel(id: String)
}