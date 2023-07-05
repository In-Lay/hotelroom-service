package com.inlay.hotelroomservice.presentation.viewmodels.hotels

import androidx.lifecycle.viewModelScope
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCase
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppHotelsViewModel(private val repositoryUseCase: RepositoryUseCase) : HotelsViewModel() {
    private val _hotelsDataList = MutableStateFlow<List<HotelsItemUiModel>>(listOf())
    override val hotelsDataList = _hotelsDataList

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
        }
    }
}