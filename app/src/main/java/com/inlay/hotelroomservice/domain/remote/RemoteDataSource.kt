package com.inlay.hotelroomservice.domain.remote

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.inlay.hotelroomservice.data.remote.models.hoteldetails.HotelDetailsModel
import com.inlay.hotelroomservice.data.remote.models.hotels.HotelsModel
import com.inlay.hotelroomservice.data.remote.models.searchlocation.SearchLocationModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getSearchLocationRepo(location: String): Response<SearchLocationModel>

    suspend fun getHotelsRepo(
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): Response<HotelsModel>

    suspend fun getStaysRepo(): Flow<List<HotelsItemUiModel?>>
    suspend fun addStaysRepo(hotelsItem: HotelsItemUiModel)
    suspend fun removeStaysRepo(hotelsItem: HotelsItemUiModel)

    suspend fun findItemIdToDelete(
        user: FirebaseUser?,
        databaseReference: DatabaseReference,
        hotelsItem: HotelsItemUiModel
    ): String?

    suspend fun getHotelDetailsRepo(
        id: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String = "USD"
    ): Response<HotelDetailsModel>
}