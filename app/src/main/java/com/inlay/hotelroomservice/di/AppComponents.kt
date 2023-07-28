package com.inlay.hotelroomservice.di

import androidx.room.Room
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.inlay.hotelroomservice.data.getSampleDetailsDataFromAssets
import com.inlay.hotelroomservice.data.getSampleHotelsDataFromAssets
import com.inlay.hotelroomservice.data.getSampleLocationsDataFromAssets
import com.inlay.hotelroomservice.data.local.HotelsRoomDatabase
import com.inlay.hotelroomservice.data.remote.api.HotelRoomApi
import com.inlay.hotelroomservice.data.remote.apiservice.HotelRoomApiService
import com.inlay.hotelroomservice.data.remote.apiservice.HotelRoomApiServiceImpl
import com.inlay.hotelroomservice.data.remote.makeHttpClient
import com.inlay.hotelroomservice.data.remote.makeMoshi
import com.inlay.hotelroomservice.data.remote.makeNetworkService
import com.inlay.hotelroomservice.data.repository.HotelRoomRepository
import com.inlay.hotelroomservice.data.repository.HotelRoomRepositoryImpl
import com.inlay.hotelroomservice.domain.local.LocalDataSource
import com.inlay.hotelroomservice.domain.local.LocalDataSourceImpl
import com.inlay.hotelroomservice.domain.remote.RemoteDataSource
import com.inlay.hotelroomservice.domain.remote.RemoteDataSourceImpl
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCase
import com.inlay.hotelroomservice.domain.usecase.RepositoryUseCaseImpl
import com.inlay.hotelroomservice.presentation.viewmodels.details.AppDetailsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.details.DetailsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.details.dialog.AppPlaceNearbyViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.details.dialog.PlaceNearbyViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.editprofile.AppEditProfileViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.editprofile.EditProfileViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.AppHotelsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.item.AppHotelsItemViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.item.HotelsItemViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.loginregister.AppLoginRegisterViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.loginregister.LoginRegisterViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.profile.AppProfileViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.profile.ProfileViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.search.AppSearchViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.search.SearchViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.search.item.AppSearchLocationsItemViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.search.item.SearchLocationsItemViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.userstays.AppUserStaysViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.userstays.UserStaysViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.Locale

val appModule = module {
//    single {
//        get<Application>().packageManager.getApplicationInfo(
//            get<Application>().packageName,
//            PackageManager.GET_META_DATA
//        )
//    }
//
//    single { get<ApplicationInfo>().metaData["HOTELSROOM_SERVICE_API_KEY"] }

    single {
        Room.databaseBuilder(
            androidContext(),
            HotelsRoomDatabase::class.java,
            "hotelsRoom-service_db"
        ).build()
    }
    single { get<HotelsRoomDatabase>().hotelsRoomDao() }

    single { Firebase.database("https://hotelroom-service-default-rtdb.europe-west1.firebasedatabase.app") }

    single { makeMoshi() }
    single { makeHttpClient() }
    single { makeNetworkService(moshi = get(), client = get()) }
    single { get<Retrofit>().create(HotelRoomApi::class.java) }
    single<HotelRoomApiService> { HotelRoomApiServiceImpl(hotelRoomApi = get()) }

    single { getSampleHotelsDataFromAssets(moshi = get(), context = androidContext()) }
    single { getSampleLocationsDataFromAssets(context = androidContext(), moshi = get()) }
    single { getSampleDetailsDataFromAssets(context = androidContext(), moshi = get()) }

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

    viewModel<HotelsViewModel> {
        AppHotelsViewModel(
            repositoryUseCase = get(),
            dateFormat = get()
        )
    }
    viewModel<HotelsItemViewModel> { AppHotelsItemViewModel() }

    viewModel<SearchViewModel> { AppSearchViewModel(repositoryUseCase = get()) }
    viewModel<SearchLocationsItemViewModel> { AppSearchLocationsItemViewModel() }

    viewModel<DetailsViewModel> { AppDetailsViewModel(repositoryUseCase = get()) }
    viewModel<PlaceNearbyViewModel> { AppPlaceNearbyViewModel() }

    viewModel<UserStaysViewModel> { AppUserStaysViewModel() }

    viewModel<ProfileViewModel> { AppProfileViewModel() }

    viewModel<EditProfileViewModel> { AppEditProfileViewModel() }

    viewModel<LoginRegisterViewModel> { AppLoginRegisterViewModel() }
}