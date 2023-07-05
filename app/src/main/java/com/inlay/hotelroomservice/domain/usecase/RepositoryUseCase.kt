package com.inlay.hotelroomservice.domain.usecase

import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel

interface RepositoryUseCase {
    suspend fun getSearchLocationRepo(location: String): List<SearchLocationsUiModel>

    suspend fun getHotelsRepo(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String = "USD"
    ): List<HotelsItemUiModel>

//    suspend fun getHotelDetailsRepo(
//        id: String,
//        checkInDate: String,
//        checkOutDate: String,
//        currencyCode: String
//    ): List<Data>
}