package com.inlay.hotelroomservice.domain

import com.inlay.hotelroomservice.data.models.hotels.Data

interface RepositoryUseCase {
    suspend fun getAllRepos(isOnline: Boolean): List<Data>
}