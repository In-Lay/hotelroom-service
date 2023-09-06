package com.inlay.hotelroomservice.presentation.viewmodels.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.inlay.hotelroomservice.CoroutineTestRule
import com.inlay.hotelroomservice.domain.usecase.details.GetHotelDetails
import com.inlay.hotelroomservice.domain.usecase.hotels.GetHotelsRepo
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*


internal class AppDetailsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val getHotelDetails = mockk<GetHotelDetails>(relaxed = true)

    private val searchModel =
        HotelDetailsSearchModel("12345", DatesModel("30.08.2023", "31.08.2023"), "USD")

    private lateinit var viewModel: AppDetailsViewModel

    @Before
    fun setUp() {
        viewModel = AppDetailsViewModel(getHotelDetails)
    }

    @Test
    fun openImageDialog() {
        val openImageDialogMock = mockk<() -> Unit>(relaxed = true)

        viewModel.initializeData(openImageDialogMock, {}, {}, {}, searchModel)
        viewModel.openImageDialog()

        verify { openImageDialogMock.invoke() }
    }

    @Test
    fun viewAllRestaurants() {
        val viewAllRestaurantsMock = mockk<() -> Unit>(relaxed = true)

        viewModel.initializeData({}, viewAllRestaurantsMock, {}, {}, searchModel)
        viewModel.viewAllRestaurants()

        verify { viewAllRestaurantsMock.invoke() }
    }

    @Test
    fun viewAllAttractions() {
        val viewAllAttractionsMock = mockk<() -> Unit>(relaxed = true)

        viewModel.initializeData({}, {}, viewAllAttractionsMock, {}, searchModel)
        viewModel.viewAllAttractions()

        verify { viewAllAttractionsMock.invoke() }
    }

    @Test
    fun openLinkInBrowser() {
        val openLinkInBrowserMock = mockk<(String) -> Unit>(relaxed = true)

        viewModel.initializeData({}, {}, {}, openLinkInBrowserMock, searchModel)
        viewModel.openLinkInBrowser()

        verify { openLinkInBrowserMock.invoke(any()) }
    }
}