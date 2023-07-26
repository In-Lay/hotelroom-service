package com.inlay.hotelroomservice.presentation.models.hotelsitem

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DatesModel(
    val checkInDate: String,
    val checkOutDate: String

) : Parcelable {
    override fun toString(): String {
        return "$checkInDate - $checkOutDate"
    }
}
