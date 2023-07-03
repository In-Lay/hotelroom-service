package com.inlay.hotelroomservice.di

import com.inlay.hotelroomservice.data.RetrofitObject
import com.inlay.hotelroomservice.data.api.HotelRoomApi
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.AppHotelsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.HotelsViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.item.AppHotelsItemViewModel
import com.inlay.hotelroomservice.presentation.viewmodels.hotels.item.HotelsItemViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single { RetrofitObject.makeRetrofit }
    single { get<Retrofit>().create(HotelRoomApi::class.java) }


    viewModel<HotelsViewModel> { AppHotelsViewModel(get()) }

    viewModel<HotelsItemViewModel> { AppHotelsItemViewModel() }
}