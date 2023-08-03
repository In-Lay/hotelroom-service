package com.inlay.hotelroomservice.data.repository

import com.inlay.hotelroomservice.data.mapping.toEntity
import com.inlay.hotelroomservice.data.mapping.toUiItem
import com.inlay.hotelroomservice.data.mapping.toUiModel
import com.inlay.hotelroomservice.data.remote.models.hoteldetails.HotelDetailsModel
import com.inlay.hotelroomservice.data.remote.models.hotels.HotelsModel
import com.inlay.hotelroomservice.data.remote.models.searchlocation.SearchLocationModel
import com.inlay.hotelroomservice.domain.local.LocalDataSource
import com.inlay.hotelroomservice.domain.remote.RemoteDataSource
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HotelRoomRepositoryImpl(
    private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource
) : HotelRoomRepository, KoinComponent {
    //Works, uncomment
    override suspend fun getSearchLocationRepo(location: String): List<SearchLocationsUiModel> {
        //Network
//        val searchLocationData = remoteDataSource.getSearchLocationRepo(location)
//        return if (searchLocationData.isSuccessful) {
//            searchLocationData.body()?.data?.map {
//                it.toUiItem()
//            }.orEmpty()
//        } else listOf()

        //JSon
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
            //Network
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

            //JSon
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


    override suspend fun getHotelDetails(
        id: String, checkInDate: String, checkOutDate: String, currencyCode: String
    ): HotelDetailsUiModel {
//        val hotelDetailsData =
//            remoteDataSource.getHotelDetailsRepo(id, checkInDate, checkOutDate, currencyCode)
//        val emptyModel = HotelDetailsUiModel(
//            "",
//            "",
//            "",
//            "",
//            "",
//            "",
//            listOf(),
//            "",
//            listOf(),
//            listOf(),
//            "",
//            listOf(),
//            listOf(),
//            0.0,
//            0.0
//        )
        //TODO use .mapNotNull{} instead emptyModel
//        return if (hotelDetailsData.isSuccessful) {
//            hotelDetailsData.body()?.toUiModel() ?: emptyModel
//        } else emptyModel

        val hotelDetailsData: HotelDetailsModel? by inject()
        val emptyModel = HotelDetailsUiModel(
            "",
            "",
            "",
            "",
            "",
            "",
            listOf(),
            "",
            listOf(),
            listOf(),
            "",
            listOf(),
            listOf(),
            0.0,
            0.0
        )

        return hotelDetailsData?.toUiModel() ?: emptyModel
    }


    //TODO ReFetch repos after user was offline
    override suspend fun getStaysRepo(
        isOnline: Boolean,
        isLogged: Boolean
    ): Flow<List<HotelsItemUiModel?>> {
        return if (isOnline && isLogged) remoteDataSource.getStaysRepo()
        else localDataSource.fetchStaysRepo().map { list ->
            list.map { it.toUiItem() }
        }
    }

    override suspend fun addStaysRepo(
        hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean
    ) {

        if (isOnline && isLogged) {
            remoteDataSource.addStaysRepo(hotelsItem)
            localDataSource.insertStayRepo(hotelsItem.toEntity())
        } else localDataSource.insertStayRepo(hotelsItem.toEntity())
    }

    override suspend fun removeStaysRepo(
        hotelsItem: HotelsItemUiModel, isOnline: Boolean, isLogged: Boolean
    ) {
        if (isOnline && isLogged) {
            remoteDataSource.removeStaysRepo(hotelsItem)
            localDataSource.deleteStayRepo(hotelsItem.toEntity())
        } else localDataSource.deleteStayRepo(hotelsItem.toEntity())
    }
}