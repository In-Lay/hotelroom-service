package com.inlay.hotelroomservice.domain.usecase.location

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel

class GetSearchLocationRepoImpl(private val repository: HotelRoomRepository) :
    GetSearchLocationRepo {
    override suspend fun invoke(location: String): List<SearchLocationsUiModel> {
        return repository.getSearchLocationRepo(location)
    }
}