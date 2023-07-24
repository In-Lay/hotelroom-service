package com.inlay.hotelroomservice.presentation.models.details

import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import kotlinx.serialization.Serializable

@Serializable
data class HotelDetailsSearchModel(
    val id: String,
    val dates: DatesModel,
    val currency: String
)
