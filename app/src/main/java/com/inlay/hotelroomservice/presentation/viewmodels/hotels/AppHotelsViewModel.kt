package com.inlay.hotelroomservice.presentation.viewmodels.hotels

import androidx.lifecycle.viewModelScope
import com.inlay.hotelroomservice.data.models.hotels.Data
import com.inlay.hotelroomservice.domain.RepositoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppHotelsViewModel(private val repositoryUseCase: RepositoryUseCase) : HotelsViewModel() {
    private val _hotelsDataList = MutableStateFlow<List<Data>>(listOf())
    override val hotelsDataList = _hotelsDataList

    override fun getRepos(isOnline: Boolean) {
        viewModelScope.launch {
            _hotelsDataList.value = repositoryUseCase.getAllRepos(isOnline)
        }
    }
}