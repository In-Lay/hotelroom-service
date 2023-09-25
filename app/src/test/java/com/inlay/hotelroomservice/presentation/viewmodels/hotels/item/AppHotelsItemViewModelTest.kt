package com.inlay.hotelroomservice.presentation.viewmodels.hotels.item

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.inlay.hotelroomservice.CoroutineTestRule
import com.inlay.hotelroomservice.presentation.models.details.HotelDetailsSearchModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsDatesAndCurrencyModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


internal class AppHotelsItemViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: AppHotelsItemViewModel

    @Before
    fun setUp() {
        viewModel = AppHotelsItemViewModel()
    }

    @Test
    fun goToDetails() {
        val goToDetailsMock = mockk<(HotelDetailsSearchModel) -> Unit>(relaxed = true)

        val hotelsUiModelMock = mockk<HotelsItemUiModel>(relaxed = true)
        val hotelsDatesAndCurrencyModelMock = mockk<HotelsDatesAndCurrencyModel>(relaxed = true)

        every { hotelsUiModelMock.title } returns "hotel name"

        val hotelDetailsSearchModel = HotelDetailsSearchModel(
            hotelsUiModelMock.id,
            hotelsDatesAndCurrencyModelMock.datesModel,
            hotelsDatesAndCurrencyModelMock.currency
        )

        viewModel.initializeData(
            hotelsUiModelMock,
            hotelsDatesAndCurrencyModelMock,
            goToDetailsMock
        ) {}

        viewModel.goToDetails()

        verify { goToDetailsMock.invoke(hotelDetailsSearchModel) }
    }

    @Test
    fun addRemoveStay() {
        val addOrRemoveStayMock = mockk<(HotelsItemUiModel) -> Unit>(relaxed = true)
        val hotelsItemDataMock = mockk<HotelsItemUiModel>(relaxed = true)
        val datesAndCurrencyMock = mockk<HotelsDatesAndCurrencyModel>(relaxed = true)

        every { hotelsItemDataMock.title } returns "hotel name"

        viewModel.initializeData(hotelsItemDataMock, datesAndCurrencyMock, {}, addOrRemoveStayMock)

        viewModel.addRemoveStay()

        verify { addOrRemoveStayMock.invoke(hotelsItemDataMock) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun initializeData() = runTest {
        val hotelsUiModelMock = mockk<HotelsItemUiModel>(relaxed = true)
        val hotelsDatesAndCurrencyModel =
            HotelsDatesAndCurrencyModel(DatesModel("30.08.2023", "31.08.2023"), "USD")

        every { hotelsUiModelMock.title } returns "hotel name"
        every { hotelsUiModelMock.id } returns "12345"

        val hotelDetailsSearchModel = HotelDetailsSearchModel(
            hotelsUiModelMock.id,
            hotelsDatesAndCurrencyModel.datesModel,
            hotelsDatesAndCurrencyModel.currency
        )

        viewModel.initializeData(hotelsUiModelMock, hotelsDatesAndCurrencyModel, {}, {})


        val hotelsModelObserver = Observer<HotelsItemUiModel?> { }
        val stringObserver = Observer<String> {}

        viewModel.apply {
            hotelItemData.observeForever(hotelsModelObserver)
            hotelName.observeForever(stringObserver)
        }
        runCurrent()
        assertEquals(hotelsUiModelMock, viewModel.hotelItemData.value)
        assertEquals("hotel name", viewModel.hotelName.value)

        viewModel.apply {
            hotelItemData.removeObserver(hotelsModelObserver)
            hotelName.removeObserver(stringObserver)
        }

        advanceUntilIdle()
        assertEquals(hotelDetailsSearchModel, viewModel.hotelDetailsSearchModel.value)
    }
}