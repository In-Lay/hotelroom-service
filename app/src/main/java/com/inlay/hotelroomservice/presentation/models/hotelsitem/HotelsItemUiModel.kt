package com.inlay.hotelroomservice.presentation.models.hotelsitem

data class HotelsItemUiModel(
    val id: String,
    val title: String,
    val hotelInfo: String,
    val rating: RatingUiModel,
    val price: String,
    val photosUrls: List<String> = listOf()
)
