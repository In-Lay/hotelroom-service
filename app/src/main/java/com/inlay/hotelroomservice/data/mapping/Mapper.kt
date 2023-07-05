package com.inlay.hotelroomservice.data.mapping

import com.inlay.hotelroomservice.data.local.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.remote.models.hotels.CardPhoto
import com.inlay.hotelroomservice.data.remote.models.hotels.Data
import com.inlay.hotelroomservice.data.remote.models.searchlocation.Image
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsImageUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel

fun Data.toUiItem(): HotelsItemUiModel = HotelsItemUiModel(
    id = this.id,
    title = this.title,
    hotelInfo = this.secondaryInfo,
    rating = this.bubbleRating.rating.toString(),
    price = this.priceForDisplay,
    photosUrls = this.cardPhotos.toHotelsItemPhotos()
)

private fun List<CardPhoto>.toHotelsItemPhotos(): List<String> {
    return this.map {
        it.sizes?.urlTemplate ?: ""
    }
}

fun com.inlay.hotelroomservice.data.remote.models.searchlocation.Data.toUiItem(): SearchLocationsUiModel =
    SearchLocationsUiModel(
        title = this.title,
        geoId = this.geoId.extractNumber(),
        secondaryText = this.secondaryText,
        image = this.image?.toSearchLocationsImageUiModel()
    )

private fun Image.toSearchLocationsImageUiModel(): SearchLocationsImageUiModel =
    SearchLocationsImageUiModel(
        typename = this.typename, urlTemplate = this.urlTemplate
    )

private fun String.extractNumber(): String? {
    val startIndex = this.indexOf(';') + 1
    val endIndex = this.indexOf(';', startIndex)
    return if (startIndex in 1 until endIndex) {
        this.substring(startIndex, endIndex)
    } else if (startIndex > 0 && endIndex == -1) {
        this.substring(startIndex)
    } else {
        null
    }
}

fun HotelsItemEntity.toUiItem(): HotelsItemUiModel = HotelsItemUiModel(
    id = this.id.toString(),
    title = this.title,
    hotelInfo = this.hotelInfo,
    rating = this.rating,
    price = this.price,
    photosUrls = this.cardPhotos
)

fun Data.toEntity(): HotelsItemEntity = HotelsItemEntity(
    id = this.id.toInt(),
    title = this.title,
    hotelInfo = this.secondaryInfo,
    rating = this.bubbleRating.rating.toString(),
    price = this.priceForDisplay,
    cardPhotos = this.cardPhotos.toHotelsItemPhotos()
)


