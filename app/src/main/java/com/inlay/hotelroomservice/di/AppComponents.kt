package com.inlay.hotelroomservice.di

import androidx.room.Room
import com.inlay.hotelroomservice.data.local.HotelsRoomDatabase
import com.inlay.hotelroomservice.data.local.api.HotelRoomApi
import com.inlay.hotelroomservice.data.remote.RetrofitObject
import com.inlay.hotelroomservice.data.remote.apiservice.HotelRoomApiService
import com.inlay.hotelroomservice.data.remote.apiservice.HotelRoomApiServiceImpl
import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import com.inlay.hotelroomservice.data.repository.HotelRoomRepositoryImpl
import com.inlay.hotelroomservice.domain.local.LocalDataSource
import com.inlay.hotelroomservice.domain.local.LocalDataSourceImpl
import com.inlay.hotelroomservice.domain.remote.RemoteDataSource
import com.inlay.hotelroomservice.domain.remote.RemoteDataSourceImpl
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCase
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCaseImpl
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.AppHotelsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.item.AppHotelsItemViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.item.HotelsItemViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.Locale

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            HotelsRoomDatabase::class.java,
            "hotelsRoom-service_db"
        ).build()
    }
    single { get<HotelsRoomDatabase>().hotelsRoomDao() }

    single { RetrofitObject.makeRetrofit }
    single { get<Retrofit>().create(HotelRoomApi::class.java) }
    single<HotelRoomApiService> { HotelRoomApiServiceImpl(hotelRoomApi = get()) }

    single<RemoteDataSource> { RemoteDataSourceImpl(hotelRoomApiService = get()) }
    single<LocalDataSource> { LocalDataSourceImpl(hotelsRoomDao = get()) }

    single<HotelRoomRepository> {
        HotelRoomRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        )
    }

    factory<RepositoryUseCase> { RepositoryUseCaseImpl(hotelRoomRepository = get()) }

    single { SimpleDateFormat("yyy-MM-dd", Locale.ENGLISH) }

    viewModel<HotelsViewModel> { AppHotelsViewModel(get()) }
    viewModel<HotelsItemViewModel> { AppHotelsItemViewModel() }
}