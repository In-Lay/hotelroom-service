package com.inlay.hotelroomservice.data

import android.content.Context
import com.inlay.hotelroomservice.data.remote.models.hoteldetails.HotelDetailsModel
import com.inlay.hotelroomservice.data.remote.models.hotels.HotelsModel
import com.inlay.hotelroomservice.data.remote.models.searchlocation.SearchLocationModel
import com.squareup.moshi.Moshi

fun getSampleHotelsDataFromAssets(context: Context, moshi: Moshi): HotelsModel? {
    val jsonString = readJson(context, "hotels_json_sample.json")

    return jsonString?.let { moshi.adapter(HotelsModel::class.java).fromJson(it) }
}

fun getSampleLocationsDataFromAssets(context: Context, moshi: Moshi): SearchLocationModel? {
    val jsonString = readJson(context, "locations_json_sample.json")

    return jsonString?.let { moshi.adapter(SearchLocationModel::class.java).fromJson(it) }
}

fun getSampleDetailsDataFromAssets(context: Context, moshi: Moshi): HotelDetailsModel? {
    val jsonString = readJson(context, "details_json_sample.json")

    return jsonString?.let { moshi.adapter(HotelDetailsModel::class.java).fromJson(it) }
}

private fun readJson(context: Context, src: String): String? {
    val jsonString: String?
    try {
        jsonString = context.assets.open(src).bufferedReader()
            .use { it.readText() }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
    return jsonString
}