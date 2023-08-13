package com.inlay.hotelroomservice.domain.usecase.stays.get

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.flow.Flow

class GetStayImpl(private val repository: HotelRoomRepository) : GetStay {
    override suspend fun invoke(
        isOnline: Boolean,
        isLogged: Boolean
    ): Flow<List<HotelsItemUiModel?>> {
        return repository.getStaysRepo(isOnline, isLogged)
    }
}