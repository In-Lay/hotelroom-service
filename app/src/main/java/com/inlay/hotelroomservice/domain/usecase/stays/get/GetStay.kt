package com.inlay.hotelroomservice.domain.usecase.stays.get

import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.flow.Flow

interface GetStay {
    suspend operator fun invoke(
        isOnline: Boolean,
        isLogged: Boolean
    ): Flow<List<HotelsItemUiModel?>>
}