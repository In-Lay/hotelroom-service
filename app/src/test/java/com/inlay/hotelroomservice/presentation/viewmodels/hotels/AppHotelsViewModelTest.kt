package com.inlay.hotelroomservice.presentation.viewmodels.hotels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.FirebaseUser
import com.inlay.hotelroomservice.CoroutineTestRule
import com.inlay.hotelroomservice.domain.usecase.datastore.nightmode.GetNightMode
import com.inlay.hotelroomservice.domain.usecase.hotels.GetHotelsRepo
import com.inlay.hotelroomservice.domain.usecase.sharedpreferences.GetLanguagePreferences
import com.inlay.hotelroomservice.domain.usecase.sharedpreferences.SaveLanguagePreferences
import com.inlay.hotelroomservice.domain.usecase.stays.add.AddStays
import com.inlay.hotelroomservice.domain.usecase.stays.get.GetStay
import com.inlay.hotelroomservice.domain.usecase.stays.remove.RemoveStay
import com.inlay.hotelroomservice.presentation.models.hotelsitem.DatesModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsDatesAndCurrencyModel
import com.inlay.hotelroomservice.presentation.models.hotelsitem.HotelsItemUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.text.SimpleDateFormat


internal class AppHotelsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val getHotelsRepoUseCase: GetHotelsRepo = mockk(relaxed = true)
    private val getStay: GetStay = mockk(relaxed = true)
    private val addStays: AddStays = mockk(relaxed = true)
    private val removeStayUseCase: RemoveStay = mockk(relaxed = true)
    private val getLanguagePreferences: GetLanguagePreferences = mockk(relaxed = true)
    private val saveLanguagePreferences: SaveLanguagePreferences = mockk(relaxed = true)
    private val getNightMode: GetNightMode = mockk(relaxed = true)
    private val dateFormat: SimpleDateFormat = mockk(relaxed = true)

    private val firebaseUser: FirebaseUser = mockk(relaxed = true)

    private lateinit var viewModel: AppHotelsViewModel

    private val langFlow = MutableSharedFlow<String>()
    private val darkModeFlow = MutableSharedFlow<Int>()

    @Before
    fun setUp() {

        every { getLanguagePreferences.getLanguage() } returns langFlow.asSharedFlow().toString()
        langFlow.tryEmit("en")
        coEvery { getNightMode.invoke() } returns darkModeFlow.asSharedFlow()
        darkModeFlow.tryEmit(2)

        viewModel = AppHotelsViewModel(
            getHotelsRepoUseCase,
            getStay,
            addStays,
            removeStayUseCase,
            getLanguagePreferences,
            saveLanguagePreferences,
            getNightMode,
            dateFormat
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun initialize() = runTest {
        every { dateFormat.format(any()) } returns "30.08.2023"

        viewModel.initialize(true, firebaseUser)

        advanceUntilIdle()
        assertEquals(true, viewModel.isOnline.value)
        assertEquals(firebaseUser, viewModel.user.value)
        assertEquals(
            HotelsDatesAndCurrencyModel(DatesModel("30.08.2023", "30.08.2023"), "USD"),
            viewModel.hotelsDatesAndCurrencyModel.value
        )

        getHotelsRepo()

        getStaysRepo()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun changeLanguage() = runTest {
        val langCode = "es"

        viewModel.changeLanguage(langCode)

        advanceUntilIdle()
        assertEquals(langCode, viewModel.language.value)

        coVerify { saveLanguagePreferences.saveLanguage(langCode) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun changeNotificationsAvailability() = runTest {
        viewModel.changeNotificationsAvailability(true)

        advanceUntilIdle()
        assertEquals(true, viewModel.notificationsAvailability.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getHotelsRepo() = runTest {
        val datesAndCurrency =
            HotelsDatesAndCurrencyModel(DatesModel("30.08.2023", "30.08.2023"), "USD")

        viewModel.getHotelsRepo(true, "12345", "30.08.2023", "30.08.2023", "USD")

        coVerify { getHotelsRepoUseCase.invoke(true, "12345", "30.08.2023", "30.08.2023", "USD") }

        advanceUntilIdle()
        assertEquals(datesAndCurrency, viewModel.hotelsDatesAndCurrencyModel.value)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getStaysRepo() = runTest {
        val hotelItemMock = mockk<HotelsItemUiModel>(relaxed = true)
        val dataFlow = MutableStateFlow(listOf(hotelItemMock))

        coEvery { getStay.invoke(isOnline = true, isLogged = true) } returns dataFlow

        viewModel.getStaysRepo(isOnline = true, isLogged = true)

        coVerify { getStay.invoke(isOnline = true, isLogged = true) }

        advanceUntilIdle()
        assertEquals(dataFlow.first(), viewModel.selectedHotelsDataList.value)
    }

    @Test
    fun addStay() = runTest {
        val hotelItemDataMock = mockk<HotelsItemUiModel>(relaxed = true)

        viewModel.addStay(hotelItemDataMock, isOnline = true, isLogged = true)

        coVerify { addStays.invoke(hotelItemDataMock, isOnline = true, isLogged = true) }
    }

    @Test
    fun removeStay() = runTest {
        val hotelItemDataMock = mockk<HotelsItemUiModel>(relaxed = true)

        viewModel.removeStay(hotelItemDataMock, isOnline = true, isLogged = true)

        coVerify { removeStayUseCase.invoke(hotelItemDataMock, isOnline = true, isLogged = true) }
    }
}