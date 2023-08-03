package com.inlay.hotelroomservice.presentation.models.hotelsitem

data class HotelsItemUiModel(
    val id: String = "",
    val title: String = "",
    val hotelInfo: String = "",
    val rating: String = "",
    val ratingCount: String = "",
    val price: String = "",
    val photosUrls: List<String> = listOf()
)
