package com.inlay.hotelroomservice.data.remote.models.hoteldetails

import com.inlay.hotelroomservice.data.local.models.hoteldetails.About
import com.inlay.hotelroomservice.data.local.models.hoteldetails.AmenitiesScreen
import com.inlay.hotelroomservice.data.local.models.hoteldetails.AttractionsNearby
import com.inlay.hotelroomservice.data.local.models.hoteldetails.GeoPoint
import com.inlay.hotelroomservice.data.local.models.hoteldetails.Location
import com.inlay.hotelroomservice.data.local.models.hoteldetails.Photo
import com.inlay.hotelroomservice.data.local.models.hoteldetails.Price
import com.inlay.hotelroomservice.data.local.models.hoteldetails.QA
import com.inlay.hotelroomservice.data.local.models.hoteldetails.RestaurantsNearby
import com.inlay.hotelroomservice.data.local.models.hoteldetails.Reviews
import com.squareup.moshi.Json

data class Data(
    @Json(name = "photos") val photos: List<Photo>,
    @Json(name = "title") val title: String,
    @Json(name = "rating") val rating: Double,
    @Json(name = "numberReviews") val numberReviews: Int,
    @Json(name = "rankingDetails") val rankingDetails: String,
    @Json(name = "price") val price: Price,
    @Json(name = "about") val about: About,
    @Json(name = "reviews") val reviews: Reviews,
    @Json(name = "location") val location: Location,
    @Json(name = "geoPoint") val geoPoint: GeoPoint,
    @Json(name = "restaurantsNearby") val restaurantsNearby: RestaurantsNearby,
    @Json(name = "attractionsNearby") val attractionsNearby: AttractionsNearby,
    @Json(name = "qA") val qA: QA,
    @Json(name = "amenitiesScreen") val amenitiesScreen: List<AmenitiesScreen>
)