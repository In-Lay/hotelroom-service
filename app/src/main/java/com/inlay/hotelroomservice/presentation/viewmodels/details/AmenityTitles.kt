package com.inlay.hotelroomservice.presentation.viewmodels.details

enum class AmenityTitles(val titles: List<String>) {
    PARKING(listOf("parking")),
    INTERNET(listOf("internet", "wifi")),
    FITNESS(listOf("fitness", "gym", "workout")),
    BAR(listOf("bar", "lounge", "cafe", "restaurant"))
}