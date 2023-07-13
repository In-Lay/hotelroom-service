package com.inlay.hotelroomservice.presentation.viewmodels.hotels.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.flow.StateFlow

abstract class HotelsItemViewModel : ViewModel() {
    abstract val hotelId: StateFlow<String>
    abstract val hotelName: LiveData<String>
    abstract val hotelInfo: LiveData<String>
    abstract val rating: LiveData<String>
    abstract val ratingCount: LiveData<String>
    abstract val price: LiveData<String>
    abstract val imageUrl: LiveData<String>

    abstract fun goToDetails()

    abstract fun initializeData(
        hotelsUiModel: HotelsItemUiModel,
        openDetails: (String) -> Unit
    )
}