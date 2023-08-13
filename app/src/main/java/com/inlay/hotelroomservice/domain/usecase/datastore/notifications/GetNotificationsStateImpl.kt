package com.inlay.hotelroomservice.domain.usecase.datastore.notifications

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationsStateImpl(private val repository: HotelRoomRepository) :
    GetNotificationsState {
    override suspend fun invoke(): Flow<Boolean> {
        return repository.getNotificationsState()
    }
}