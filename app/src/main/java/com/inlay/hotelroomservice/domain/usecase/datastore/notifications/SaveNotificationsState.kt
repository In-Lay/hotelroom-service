package com.inlay.hotelroomservice.domain.usecase.datastore.notifications

interface SaveNotificationsState {
    suspend operator fun invoke(notificationsState: Boolean)
}