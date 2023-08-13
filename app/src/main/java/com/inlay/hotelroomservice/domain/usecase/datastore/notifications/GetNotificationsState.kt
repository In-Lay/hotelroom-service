package com.inlay.hotelroomservice.domain.usecase.datastore.notifications

import kotlinx.coroutines.flow.Flow


interface GetNotificationsState {
    suspend operator fun invoke(): Flow<Boolean>
}