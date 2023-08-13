package com.inlay.hotelroomservice.domain.usecase.stays.remove

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel

class RemoveStayImpl(private val repository: HotelRoomRepository) : RemoveStay {
    override suspend fun invoke(
        hotelsItem: HotelsItemUiModel,
        isOnline: Boolean,
        isLogged: Boolean
    ) {
        repository.removeStaysRepo(hotelsItem, isOnline, isLogged)
    }
}