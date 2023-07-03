package com.inlay.hotelroomservice.presentation.viewmodels.hotels

import androidx.lifecycle.ViewModel
import com.inlay.hotelroomservice.data.models.hotels.Data
import kotlinx.coroutines.flow.StateFlow

abstract class HotelsViewModel : ViewModel() {
    abstract val hotelsDataList: StateFlow<List<Data>>

    abstract fun getRepos(isOnline: Boolean)
}