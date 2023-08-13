package com.inlay.hotelroomservice.domain.usecase.datastore.notifications

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository

class SaveNotificationsStateImpl(private val repository: HotelRoomRepository) :
    SaveNotificationsState {
    override suspend fun invoke(notificationsState: Boolean) {
        repository.saveNotificationsState(notificationsState)
    }
}