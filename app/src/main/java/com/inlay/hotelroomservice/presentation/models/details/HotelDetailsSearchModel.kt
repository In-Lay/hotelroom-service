package com.inlay.hotelroomservice.presentation.models.details

import android.os.Parcelable
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class HotelDetailsSearchModel(
    val id: String,
    val dates: DatesModel,
    val currency: String
) : Parcelable
