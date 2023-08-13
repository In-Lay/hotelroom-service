package com.inlay.hotelroomservice.domain.usecase.datastore.nightmode

import com.inlay.hotelroomservice.data.repository.HotelRoomRepository

class SaveNightModeImpl(private val repository: HotelRoomRepository) : SaveNightMode {
    override suspend fun invoke(nightModeState: Int) {
        repository.saveNightModeState(nightModeState)
    }
}