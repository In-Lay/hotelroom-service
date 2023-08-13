package com.inlay.hotelroomservice.domain.usecase.location

import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel

interface GetSearchLocationRepo {
    suspend operator fun invoke(location: String): List<SearchLocationsUiModel>
}