package com.inlay.hotelroomservice.presentation.models.hotelsitem

data class DatesModel(
    val checkInDate: String,
    val checkOutDate: String

) {
    override fun toString(): String {
        return "$checkInDate - $checkOutDate"
    }
}
