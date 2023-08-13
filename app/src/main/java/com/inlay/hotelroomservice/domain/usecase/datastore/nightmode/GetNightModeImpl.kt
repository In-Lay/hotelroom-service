package com.inlay.hotelroomservice.domain.usecase.datastore.nightmode

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import kotlinx.coroutines.flow.Flow

class GetNightModeImpl(private val repository: HotelRoomRepository) : GetNightMode {
    override suspend fun invoke(): Flow<Int> {
        return repository.getNightModeState()
    }
}