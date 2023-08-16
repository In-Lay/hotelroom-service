package com.inlay.hotelroomservice.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.inlay.hotelroomservice.data.getSampleDetailsDataFromAssets
import com.inlay.hotelroomservice.data.getSampleHotelsDataFromAssets
import com.inlay.hotelroomservice.data.getSampleLocationsDataFromAssets
import com.inlay.hotelroomservice.data.local.database.HotelsRoomDatabase
import com.inlay.hotelroomservice.data.local.datastore.AppSettingsDataStore
import com.inlay.hotelroomservice.data.local.datastore.SettingsDataStore
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
import com.inlay.hotelroomservice.domain.usecase.datastore.nightmode.GetNightMode
import com.inlay.hotelroomservice.domain.usecase.datastore.nightmode.GetNightModeImpl
import com.inlay.hotelroomservice.domain.usecase.datastore.nightmode.SaveNightMode
import com.inlay.hotelroomservice.domain.usecase.datastore.nightmode.SaveNightModeImpl
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.GetNotificationsState
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.GetNotificationsStateImpl
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.SaveNotificationsState
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.SaveNotificationsStateImpl
import com.inlay.hotelroomservice.domain.usecase.details.GetHotelDetails
import com.inlay.hotelroomservice.domain.usecase.details.GetHotelDetailsImpl
import com.inlay.hotelroomservice.domain.usecase.hotels.GetHotelsRepo
import com.inlay.hotelroomservice.domain.usecase.hotels.GetHotelsRepoImpl
import com.inlay.hotelroomservice.domain.usecase.location.GetSearchLocationRepo
import com.inlay.hotelroomservice.domain.usecase.location.GetSearchLocationRepoImpl
import com.inlay.hotelroomservice.domain.usecase.sharedpreferences.GetLanguagePreferences
import com.inlay.hotelroomservice.domain.usecase.sharedpreferences.GetLanguagePreferencesImpl
import com.inlay.hotelroomservice.domain.usecase.sharedpreferences.SaveLanguagePreferences
import com.inlay.hotelroomservice.domain.usecase.sharedpreferences.SaveLanguagePreferencesImpl
import com.inlay.hotelroomservice.domain.usecase.stays.add.AddStays
import com.inlay.hotelroomservice.domain.usecase.stays.add.AddStaysImpl
import com.inlay.hotelroomservice.domain.usecase.stays.get.GetStay
import com.inlay.hotelroomservice.domain.usecase.stays.get.GetStayImpl
import com.inlay.hotelroomservice.domain.usecase.stays.remove.RemoveStay
import com.inlay.hotelroomservice.domain.usecase.stays.remove.RemoveStayImpl
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
import com.inlay.hotelroomservice.presentation.viewmodels.settings.AppSettingsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.settings.SettingsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.splash.AppSplashViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.splash.SplashViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.userstays.AppUserStaysViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.userstays.UserStaysViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.Locale

private const val SHARED_PREFS_KEY = "LANG_SHRED_PREFS_KEY"
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

    single<SettingsDataStore> { AppSettingsDataStore(context = androidContext()) }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            SHARED_PREFS_KEY,
            Context.MODE_PRIVATE
        )
    }
    factory<GetLanguagePreferences> { GetLanguagePreferencesImpl(sharedPreferences = get()) }
    factory<SaveLanguagePreferences> { SaveLanguagePreferencesImpl(sharedPreferences = get()) }

    single<RemoteDataSource> { RemoteDataSourceImpl(hotelRoomApiService = get(), database = get()) }
    single<LocalDataSource> {
        LocalDataSourceImpl(
            hotelsRoomDao = get(),
            settingsDataStore = get()
        )
    }

    single<HotelRoomRepository> {
        HotelRoomRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        )
    }


    factory<GetHotelsRepo> { GetHotelsRepoImpl(hotelRoomRepository = get()) }

    factory<GetSearchLocationRepo> { GetSearchLocationRepoImpl(repository = get()) }

    factory<GetHotelDetails> { GetHotelDetailsImpl(repository = get()) }

    factory<GetStay> { GetStayImpl(repository = get()) }
    factory<AddStays> { AddStaysImpl(repository = get()) }
    factory<RemoveStay> { RemoveStayImpl(repository = get()) }

    factory<GetNightMode> { GetNightModeImpl(repository = get()) }
    factory<SaveNightMode> { SaveNightModeImpl(repository = get()) }

    factory<GetNotificationsState> { GetNotificationsStateImpl(repository = get()) }
    factory<SaveNotificationsState> { SaveNotificationsStateImpl(repository = get()) }


    single { SimpleDateFormat("yyy-MM-dd", Locale.ENGLISH) }


    viewModel<SplashViewModel> { AppSplashViewModel(saveNotificationsState = get()) }

    viewModel<HotelsViewModel> {
        AppHotelsViewModel(
            getHotelsRepoUseCase = get(),
            getStay = get(),
            addStays = get(),
            removeStayUseCase = get(),
            dateFormat = get(),
            getLanguagePreferences = get(),
            saveLanguagePreferences = get(),
            getNightMode = get()
        )
    }
    viewModel<HotelsItemViewModel> { AppHotelsItemViewModel() }

    viewModel<SearchViewModel> {
        AppSearchViewModel(
            getSearchLocationRepo = get()
        )
    }
    viewModel<SearchLocationsItemViewModel> { AppSearchLocationsItemViewModel() }

    viewModel<DetailsViewModel> { AppDetailsViewModel(getHotelDetails = get()) }
    viewModel<PlaceNearbyViewModel> { AppPlaceNearbyViewModel() }

    viewModel<UserStaysViewModel> { AppUserStaysViewModel() }

    viewModel<ProfileViewModel> { AppProfileViewModel() }
    viewModel<EditProfileViewModel> { AppEditProfileViewModel() }
    viewModel<LoginRegisterViewModel> { AppLoginRegisterViewModel() }

    viewModel<SettingsViewModel> {
        AppSettingsViewModel(
            saveNightMode = get(),
            getNotificationsState = get(),
            saveNotificationsState = get()
        )
    }
}