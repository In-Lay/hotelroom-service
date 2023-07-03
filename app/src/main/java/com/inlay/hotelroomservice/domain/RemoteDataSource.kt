package com.inlay.hotelroomservice.domain

import com.inlay.hotelroomservice.data.models.hotels.HotelsModel

interface RemoteDataSource {
    fun getRepos():List<HotelsModel>
}