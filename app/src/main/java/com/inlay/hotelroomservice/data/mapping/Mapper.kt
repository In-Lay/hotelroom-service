package com.inlay.hotelroomservice.data.mapping

import com.inlay.hotelroomservice.data.local.database.models.HotelsItemEntity
import com.inlay.hotelroomservice.data.local.database.models.HotelsItemStaysEntity
import com.inlay.hotelroomservice.data.remote.models.hoteldetails.AboutContentGeneral
import com.inlay.hotelroomservice.data.remote.models.hoteldetails.AttractionsNearbyContent
import com.inlay.hotelroomservice.data.remote.models.hoteldetails.HotelDetailsModel
import com.inlay.hotelroomservice.data.remote.models.hoteldetails.RestaurantsNearbyContent
import com.inlay.hotelroomservice.data.remote.models.hotels.CardPhoto
import com.inlay.hotelroomservice.data.remote.models.hotels.Data
import com.inlay.hotelroomservice.data.remote.models.searchlocation.Image
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.details.NearbyPlace
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsImageUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel

fun Data.toUiItem(): HotelsItemUiModel = HotelsItemUiModel(
    id = this.id,
    title = this.title.orEmpty(),
    hotelInfo = this.secondaryInfo.orEmpty(),
    rating = this.bubbleRating?.rating.toString(),
    ratingCount = this.bubbleRating?.count.toString(),
    price = this.priceForDisplay.orEmpty(),
    photosUrls = this.cardPhotos.toHotelsItemPhotos()
)

private fun List<CardPhoto>.toHotelsItemPhotos(): List<String> {
    return this.map {
        val url = it.sizes?.urlTemplate?.replace("{width}", "1200")?.replace("{height}", "1000")
        url.orEmpty()
    }
}

fun HotelsItemEntity.toUiItem(): HotelsItemUiModel = HotelsItemUiModel(
    id = this.id.toString(),
    title = this.title,
    hotelInfo = this.hotelInfo,
    rating = this.rating,
    ratingCount = this.ratingCount,
    price = this.price,
    photosUrls = this.cardPhotos.orEmpty()
)

fun Data.toEntity(): HotelsItemEntity = HotelsItemEntity(
    id = this.id.toInt(),
    title = this.title.orEmpty(),
    hotelInfo = this.secondaryInfo.orEmpty(),
    rating = this.bubbleRating?.rating.toString(),
    ratingCount = this.bubbleRating?.count.toString(),
    price = this.priceForDisplay.orEmpty(),
    cardPhotos = this.cardPhotos.toHotelsItemPhotos()
)

fun HotelsItemStaysEntity.toUiItem(): HotelsItemUiModel = HotelsItemUiModel(
    id = this.id.toString(),
    title = this.title,
    hotelInfo = this.hotelInfo,
    rating = this.rating,
    ratingCount = this.ratingCount,
    price = this.price,
    photosUrls = this.cardPhotos.orEmpty()
)

fun HotelsItemUiModel.toEntity(): HotelsItemStaysEntity = HotelsItemStaysEntity(
    id = this.id.toInt(),
    title = this.title,
    hotelInfo = this.hotelInfo,
    rating = this.rating,
    ratingCount = this.ratingCount,
    price = this.price,
    cardPhotos = this.photosUrls
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
        typename = this.typename.orEmpty(), urlTemplate = this.urlTemplate.orEmpty()
    )


fun HotelDetailsModel?.toUiModel(): HotelDetailsUiModel =
    HotelDetailsUiModel(title = this?.data?.title.orEmpty(),
        rating = this?.data?.rating.toString(),
        numberReviews = this?.data?.numberReviews.toString(),
        rankingDetails = this?.data?.rankingDetails.orEmpty(),
        displayPrice = this?.data?.price?.displayPrice.orEmpty(),
        providerName = this?.data?.price?.providerName.orEmpty(),
        photos = this?.data?.photos?.mapNotNull {
            it.urlTemplate?.replace(
                "{width}", "1200"
            )?.replace("{height}", "1000")
        }.orEmpty(),
        aboutTitle = this?.data?.about?.title.orEmpty(),
        aboutLinks = this?.data?.about?.aboutContentGeneral?.getAboutByTitle("Related links")
            .orEmpty(),
        aboutAmenities = this?.data?.about?.aboutContentGeneral?.getAboutByTitle("Amenities")
            .orEmpty(),
        address = this?.data?.location?.address.orEmpty(),
        restaurantsNearby = this?.data?.restaurantsNearby?.restaurantsNearbyContent?.map { it.toRestaurantNearby() }
            ?: listOf(),
        attractionsNearby = this?.data?.attractionsNearby?.content?.map { it.toAttractionNearby() }
            ?: listOf(),
        latitude = this?.data?.geoPoint?.latitude ?: 0.0,
        longitude = this?.data?.geoPoint?.longitude ?: 0.0)

private fun List<AboutContentGeneral>.getAboutByTitle(title: String): List<String> {
    return this.filter { aboutContent ->
        aboutContent.title == title
    }.flatMap { aboutContent ->
        aboutContent.contentAbout.mapNotNull { contentAbout ->
            when (title) {
                "Related links" -> contentAbout.content
                "Amenities" -> contentAbout.title
                else -> null
            }
        }
    }
}


private fun RestaurantsNearbyContent.toRestaurantNearby(): NearbyPlace.RestaurantNearby =
    NearbyPlace.RestaurantNearby(
        title = this.title.orEmpty(),
        primaryInfo = this.primaryInfo.orEmpty(),
        distance = this.distance.orEmpty(),
        photoUrlTemplate = this.restaurantsNearbyCardPhoto?.urlTemplate?.replace(
            "{width}", "500"
        )?.replace("{height}", "300").orEmpty(),
        rating = this.bubbleRating?.rating.toString(),
        numberReviews = this.bubbleRating?.numberReviews.orEmpty()
    )

private fun AttractionsNearbyContent.toAttractionNearby(): NearbyPlace.AttractionNearby =
    NearbyPlace.AttractionNearby(
        title = this.title.orEmpty(),
        primaryInfo = this.primaryInfo.orEmpty(),
        distance = this.distance.orEmpty(),
        photoUrlTemplate = this.cardPhoto?.urlTemplate?.replace(
            "{width}", "500"
        )?.replace("{height}", "300").orEmpty(),
        rating = this.bubbleRating?.rating.toString(),
        numberReviews = this.bubbleRating?.numberReviews.orEmpty()
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

