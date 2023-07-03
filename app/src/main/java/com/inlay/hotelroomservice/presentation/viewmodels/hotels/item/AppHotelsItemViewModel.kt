package com.inlay.hotelroomservice.presentation.viewmodels.hotels.item

import androidx.lifecycle.asLiveData
import com.inlay.hotelroomservice.data.models.hotels.CardPhoto
import kotlinx.coroutines.flow.MutableStateFlow

class AppHotelsItemViewModel : HotelsItemViewModel() {
    private val _hotelId = MutableStateFlow("")
    override val hotelId = _hotelId

    private val _hotelName = MutableStateFlow("")
    override val hotelName = _hotelName.asLiveData()

    private val _hotelInfo = MutableStateFlow("")
    override val hotelInfo = _hotelInfo.asLiveData()

    private val _rating = MutableStateFlow(0.0)
    override val rating = _rating.asLiveData()

    private val _price = MutableStateFlow("")
    override val price = _price.asLiveData()

    private val _imageUrl = MutableStateFlow<List<CardPhoto>>(listOf())
    override val imageUrl = _imageUrl.asLiveData()

    private lateinit var goToDetailsLambda: (String) -> Unit

    override fun goToDetails() {
        goToDetailsLambda(_hotelId.value)
    }

    override fun initializeData(
        hotelId: String,
        hotelName: String,
        hotelInfo: String,
        rating: Double,
        price: String,
        imageUrl: List<CardPhoto>,
        openDetails: (String) -> Unit
    ) {
        _hotelId.value = hotelId
        _hotelName.value = hotelName
        _hotelInfo.value = hotelInfo
        _rating.value = rating
        _price.value = price
        _imageUrl.value = imageUrl
        goToDetailsLambda = openDetails
    }
}