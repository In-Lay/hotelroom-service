package com.inlay.hotelroomservice.domain.usecase.stays.remove

import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel

interface RemoveStay {
    suspend operator fun invoke(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean)
}