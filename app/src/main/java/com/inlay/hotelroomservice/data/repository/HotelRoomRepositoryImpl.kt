package com.inlay.hotelroomservice.data.repository

import com.inlay.hotelroomservice.data.mapping.toEntity
import com.inlay.hotelroomservice.data.mapping.toUiItem
import com.inlay.hotelroomservice.data.remote.models.hotels.HotelsModel
import com.inlay.hotelroomservice.data.remote.models.searchlocation.SearchLocationModel
import com.inlay.hotelroomservice.domain.local.LocalDataSource
import com.inlay.hotelroomservice.domain.remote.RemoteDataSource
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HotelRoomRepositoryImpl(
    private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource
) : HotelRoomRepository, KoinComponent {
    //Works, uncomment
    override suspend fun getSearchLocationRepo(location: String): List<SearchLocationsUiModel> {
//        val searchLocationData = remoteDataSource.getSearchLocationRepo(location)
//        return if (searchLocationData.isSuccessful) {
//            searchLocationData.body()?.data?.map {
//                it.toUiItem()
//            }.orEmpty()
//        } else listOf()

        val searchLocationData: SearchLocationModel? by inject()
        return searchLocationData?.data?.map {
            it.toUiItem()
        }.orEmpty()
    }

    //Works, uncomment
    override suspend fun getHotelRepo(
        isOnline: Boolean,
        geoId: String,
        checkInDate: String,
        checkOutDate: String,
        currencyCode: String
    ): List<HotelsItemUiModel> {
        return if (isOnline) {
//            val hotelsData =
//                remoteDataSource.getHotelsRepo(geoId, checkInDate, checkOutDate, currencyCode)
//            if (hotelsData.isSuccessful) {
//                localDataSource.insertRepo(hotelsData.body()?.generalData?.dataList?.map { it.toEntity() }
//                    ?: listOf())
//                hotelsData.body()?.generalData?.dataList?.map { data ->
//                    data.toUiItem()
//                } ?: listOf()
//            } else {
//                listOf()
//            }
            val sampleData: HotelsModel? by inject()

            sampleData?.let { data ->
                localDataSource.insertRepo(data.generalData?.dataList?.map { it.toEntity() }
                    .orEmpty())
                data.generalData?.dataList?.map {
                    it.toUiItem()
                }
            }.orEmpty()


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