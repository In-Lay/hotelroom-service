package com.inlay.hotelroomservice.domain.usecase.stays.add

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel

class AddStaysImpl(private val repository: HotelRoomRepository) : AddStays {
    override suspend fun invoke(
        hotelsItem: HotelsItemUiModel,
        isOnline: Boolean,
        isLogged: Boolean
    ) {
        repository.addStaysRepo(hotelsItem, isOnline, isLogged)
    }
}