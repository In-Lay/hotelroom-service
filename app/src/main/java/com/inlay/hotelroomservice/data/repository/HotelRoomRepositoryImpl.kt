package com.inlay.hotelroomservice.data.repository

import com.inlay.hotelroomservice.data.mapping.toEntity
import com.inlay.hotelroomservice.data.mapping.toUiItem
import com.inlay.hotelroomservice.domain.local.LocalDataSource
import com.inlay.hotelroomservice.domain.remote.RemoteDataSource
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.first

class HotelRoomRepositoryImpl(
    private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource
) : HotelRoomRepository {

    override suspend fun getSearchLocationRepo(location: String): List<SearchLocationsUiModel> {
        val searchLocationData = remoteDataSource.getSearchLocationRepo(location)
        return if (searchLocationData.isSuccessful) {
            searchLocationData.body()?.data?.map {
                it.toUiItem()
            } ?: listOf()
        } else listOf()
    }

    override suspend fun getHotelRepo(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): List<HotelsItemUiModel> {
        return if (isOnline) {
            val hotelsData =
                remoteDataSource.getHotelsRepo(geoId, checkInDate, checkOutDate, currencyCode)
            if (hotelsData.isSuccessful) {
                localDataSource.insertRepo(hotelsData.body()?.generalData?.dataList?.map { it.toEntity() }
                    ?: listOf())
                hotelsData.body()?.generalData?.dataList?.map { data ->
                    data.toUiItem()
                } ?: listOf()
            } else {
                listOf()
            }
        } else {
            localDataSource.fetchRepo().first().map {
                it.toUiItem()
            }
        }
    }

//    override suspend fun getHotelDetails(
//        id: String,
//        checkInDate: String,
//        checkOutDate: String,
//        currencyCode: String
//    ): Any? {
//        val hotelDetailsData =
//            remoteDataSource.getHotelDetailsRepo(id, checkInDate, checkOutDate, currencyCode)
//        return if (hotelDetailsData.isSuccessful) {
//            hotelDetailsData.body()?.data
//        } else {
//
//        }
//    }
}