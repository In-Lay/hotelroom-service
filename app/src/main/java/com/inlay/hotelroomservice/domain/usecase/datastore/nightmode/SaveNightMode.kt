package com.inlay.hotelroomservice.domain.usecase.datastore.nightmode

interface SaveNightMode {
    suspend operator fun invoke(nightModeState: Int)
}