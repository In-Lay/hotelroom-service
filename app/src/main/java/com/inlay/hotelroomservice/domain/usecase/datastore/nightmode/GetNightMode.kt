package com.inlay.hotelroomservice.domain.usecase.datastore.nightmode

import kotlinx.coroutines.flow.Flow

interface GetNightMode {
    suspend operator fun invoke(): Flow<Int>
}