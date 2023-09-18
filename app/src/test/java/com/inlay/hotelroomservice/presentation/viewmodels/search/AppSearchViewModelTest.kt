package com.inlay.hotelroomservice.presentation.viewmodels.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.inlay.hotelroomservice.CoroutineTestRule
import com.inlay.hotelroomservice.domain.usecase.location.GetSearchLocationRepo
import com.inlay.hotelroomservice.presentation.models.SearchDataUiModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsImageUiModel
import com.inlay.hotelroomservice.presentation.models.locations.SearchLocationsUiModel
import io.mockk.coVerify
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


internal class AppSearchViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private val mockGetSearchLocationRepo: GetSearchLocationRepo = mockk(relaxed = true)

    private lateinit var viewModel: AppSearchViewModel

    @Before
    fun setUp() {
        viewModel = AppSearchViewModel(mockGetSearchLocationRepo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setDates() = runTest {
        val dates = DatesModel("30.08.2023", "31.08.2023")
        viewModel.setDates(dates)

        val observer = Observer<DatesModel> {}
        viewModel.dates.observeForever(observer)
        runCurrent()

        assertEquals(dates, viewModel.dates.value)

        viewModel.dates.removeObserver(observer)
    }

    @Test
    fun setCurrentItemModel() {
        val model = SearchLocationsUiModel("", null, "", SearchLocationsImageUiModel("", ""))

        viewModel.setCurrentItemModel(model)

        assertEquals(model, viewModel.selectedItemModel.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getSearchLocations with proper searchInput`() = runTest {
        val searchInput = "Beijing"

        viewModel.getSearchLocations(searchInput)

        assertEquals("", viewModel.supportText.value)
        advanceUntilIdle()
        coVerify { mockGetSearchLocationRepo.invoke(searchInput.lowercase()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getSearchLocations searchInput is empty`() = runTest {
        val searchInput = ""
        val supportText = "Enter location"

        viewModel.getSearchLocations(searchInput)

        val observer = Observer<String> {}
        viewModel.supportText.observeForever(observer)
        runCurrent()

        assertEquals(supportText, viewModel.supportText.value)

        viewModel.supportText.removeObserver(observer)
    }

    @Test
    fun openDatePicker() {
        val mockOpenDatePicker = mockk<() -> Unit>(relaxed = true)

        viewModel.init(true, mockOpenDatePicker) {}

        viewModel.openDatePicker()

        verify { mockOpenDatePicker() }
    }

    @Test
    fun `searchHotels with non empty Data`() = runTest {
        val dates = DatesModel("30.08.2023", "31.08.2023")
        val searchModel = SearchLocationsUiModel("", "12345", "", null)
        val searchData = SearchDataUiModel("12345", "30.08.2023", "31.08.2023", "USD")
        val mockSearchHotels = mockk<(SearchDataUiModel) -> Unit>(relaxed = true)

        viewModel.setSearchData(searchModel, dates, "USD")

        viewModel.init(true, {}, mockSearchHotels)

        viewModel.searchHotels()

        verify { mockSearchHotels.invoke(searchData) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchHotels with empty dates`() = runTest {
        val dates = DatesModel("", "")
        val searchModel = SearchLocationsUiModel("", "12345", "", null)
        val supportText = "Pick dates"

        viewModel.setSearchData(searchModel, dates, "USD")

        viewModel.searchHotels()

        val observer = Observer<String> {}
        viewModel.supportText.observeForever(observer)
        runCurrent()

        assertEquals(supportText, viewModel.supportText.value)

        viewModel.supportText.removeObserver(observer)
    }
}