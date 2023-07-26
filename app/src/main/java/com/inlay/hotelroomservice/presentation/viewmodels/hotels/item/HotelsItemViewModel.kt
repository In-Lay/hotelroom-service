package com.inlay.hotelroomservice.presentation.viewmodels.hotels.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsDatesAndCurrencyModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.flow.StateFlow

abstract class HotelsItemViewModel : ViewModel() {
    abstract val hotelItemData: LiveData<HotelsItemUiModel?>

    abstract val hotelDetailsSearchModel: StateFlow<HotelDetailsSearchModel?>

    abstract val hotelName: LiveData<String>

    abstract fun goToDetails()

    abstract fun addToStay()

    abstract fun initializeData(
        hotelsUiModel: HotelsItemUiModel,
        hotelsDatesAndCurrencyModel: HotelsDatesAndCurrencyModel,
        openDetails: (HotelDetailsSearchModel) -> Unit
    )
}