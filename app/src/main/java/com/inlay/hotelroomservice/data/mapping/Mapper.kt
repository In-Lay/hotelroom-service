package com.inlay.hotelroomservice.data.mapping

import com.inlay.hotelroomservice.data.local.models.HotelRatingEntity
import com.inlay.hotelroomservice.data.local.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.models.HotelsItemWithRatingEntity
import com.inlay.hotelroomservice.data.remote.models.hotels.BubbleRating
import com.inlay.hotelroomservice.data.remote.models.hotels.CardPhoto
import com.inlay.hotelroomservice.data.remote.models.hotels.Data
import com.inlay.hotelroomservice.data.remote.models.searchlocation.Image
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.RatingUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsImageUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel

fun Data.toUiItem(): HotelsItemUiModel = HotelsItemUiModel(
    id = this.id,
    title = this.title.orEmpty(),
    hotelInfo = this.secondaryInfo.orEmpty(),
    rating = this.bubbleRating?.toUiRatingModel() ?: RatingUiModel("", 0.0),
    price = this.priceForDisplay.orEmpty(),
    photosUrls = this.cardPhotos.toHotelsItemPhotos()
)

private fun List<CardPhoto>.toHotelsItemPhotos(): List<String> {
    return this.map {
        val url = it.sizes?.urlTemplate?.replace("{width}", "500")
            ?.replace("{height}", "300")
        url.orEmpty()
    }
}

private fun BubbleRating.toUiRatingModel(): RatingUiModel = RatingUiModel(
    count = count.orEmpty(),
    rating = rating ?: 0.0
)

fun HotelsItemWithRatingEntity.toUiItem(): HotelsItemUiModel = HotelsItemUiModel(
    id = hotelItem.id.toString(),
    title = hotelItem.title,
    hotelInfo = hotelItem.hotelInfo,
    rating = hotelItem.rating.toUiRatingModel(),
    price = hotelItem.price,
    photosUrls = hotelItem.cardPhotos.orEmpty()
)

fun Data.toEntity(): HotelsItemEntity = HotelsItemEntity(
//    hotelItem = HotelsItemEntity(
//        id = this.id.toInt(),
//        title = this.title.orEmpty(),
//        hotelInfo = this.secondaryInfo.orEmpty(),
//        rating = this.bubbleRating?.toEntityRatingModel() ?: HotelRatingEntity(0, "", 0.0),
//        price = this.priceForDisplay.orEmpty(),
//        cardPhotos = this.cardPhotos.toHotelsItemPhotos()
//    ),
//    rating = this.bubbleRating?.toEntityRatingModel() ?: HotelRatingEntity(0, "", 0.0)
    id = this.id.toInt(),
    title = this.title.orEmpty(),
    hotelInfo = this.secondaryInfo.orEmpty(),
    rating = this.bubbleRating?.toEntityRatingModel() ?: HotelRatingEntity(0, "", 0.0),
    price = this.priceForDisplay.orEmpty(),
    cardPhotos = this.cardPhotos.toHotelsItemPhotos()
)


private fun HotelRatingEntity.toUiRatingModel(): RatingUiModel = RatingUiModel(
    count = count,
    rating = rating
)

private fun BubbleRating.toEntityRatingModel(): HotelRatingEntity = HotelRatingEntity(
    ratingId = 0,
    count = count.orEmpty(),
    rating = rating ?: 0.0
)

fun com.inlay.hotelroomservice.data.remote.models.searchlocation.Data.toUiItem(): SearchLocationsUiModel =
    SearchLocationsUiModel(
        title = this.title?.removePrefix("<b>")?.removeSuffix("</b>").orEmpty(),
        geoId = this.geoId.extractNumber(),
        secondaryText = this.secondaryText.orEmpty(),
        image = this.image?.toSearchLocationsImageUiModel()
    )

private fun Image.toSearchLocationsImageUiModel(): SearchLocationsImageUiModel =
    SearchLocationsImageUiModel(
        typename = this.typename.orEmpty(),
        urlTemplate = this.urlTemplate.orEmpty()
    )

private fun String?.extractNumber(): String? {
    val startIndex = (this?.indexOf(';')?.plus(1)) ?: 0
    val endIndex = this?.indexOf(';', startIndex) ?: 0
    return if (startIndex in 1 until endIndex) {
        this?.substring(startIndex, endIndex)
    } else if (startIndex > 0 && endIndex == -1) {
        this?.substring(startIndex)
    } else {
        null
    }
}

