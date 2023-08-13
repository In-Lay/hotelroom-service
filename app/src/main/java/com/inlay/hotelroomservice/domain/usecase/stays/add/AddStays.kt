package com.inlay.hotelroomservice.domain.usecase.stays.add

import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel

interface AddStays {
    suspend operator fun invoke(hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean)
}