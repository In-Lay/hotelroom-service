package com.inlay.hotelroomservice.presentation.viewmodels.hotels.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.data.models.hotels.CardPhoto
import kotlinx.coroutines.flow.StateFlow

abstract class HotelsItemViewModel : ViewModel() {
    abstract val hotelId: StateFlow<String>
    abstract val hotelName: LiveData<String>
    abstract val hotelInfo: LiveData<String>
    abstract val rating: LiveData<Double>
    abstract val price: LiveData<String>
    abstract val imageUrl: LiveData<List<CardPhoto>>

    abstract fun goToDetails()

    abstract fun initializeData(
        hotelId: String,
        hotelName: String,
        hotelInfo: String,
        rating: Double,
        price: String,
        imageUrl: List<CardPhoto>,
        openDetails: (String) -> Unit
    )
}