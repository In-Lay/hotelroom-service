package com.inlay.hotelroomservice.presentation.viewmodels.settings

import androidx.appcompat.app.AppCompatDelegate
import com.inlay.hotelroomservice.CoroutineTestRule
import com.inlay.hotelroomservice.domain.usecase.datastore.nightmode.SaveNightMode
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.GetNotificationsState
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.SaveNotificationsState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class AppSettingsViewModelTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private val mockSaveNightMode: SaveNightMode = mockk(relaxed = true)

    private val mockGetNotificationsState: GetNotificationsState = mockk(relaxed = true)

    private val mockSaveNotificationsState: SaveNotificationsState = mockk(relaxed = true)

    private lateinit var viewModel: AppSettingsViewModel

    private val sharedFlow = MutableSharedFlow<Boolean>()

    @Before
    fun setup() {
        coEvery { mockGetNotificationsState.invoke() } returns sharedFlow.asSharedFlow()
        sharedFlow.tryEmit(true)
        viewModel = AppSettingsViewModel(
            mockSaveNightMode,
            mockGetNotificationsState,
            mockSaveNotificationsState
        )
    }

    @Test
    fun `initialize sets languages list and openLangDialog`() {
        val languagesList = listOf("English", "Spanish")
        val openLangDialogMock = mockk<() -> Unit>(relaxed = true)
        viewModel.initialize(openLangDialogMock, languagesList)

        assert(viewModel.langsList.value == languagesList)

        viewModel.openLangDialog()
        verify { openLangDialogMock.invoke() }
    }

    @Test
    fun `openLangDialog invokes the provided lambda`() {
        val openLangDialogMock = mockk<() -> Unit>(relaxed = true)
        viewModel.initialize(openLangDialogMock, listOf())

        viewModel.openLangDialog()

        verify { openLangDialogMock.invoke() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `changeNotificationsState updates notifications state and calls saveNotificationsState`() =
        runTest {
            val newState = true
            viewModel.changeNotificationsState(newState)
            val state = viewModel.inAppNotificationsState.first()
            Assert.assertEquals(newState, state)

            advanceUntilIdle()
            coVerify { mockSaveNotificationsState.invoke(newState) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onDarkModeChanged updates night mode and calls saveNightMode`() =
        runTest {
            val isChecked = true
            val expectedMode = AppCompatDelegate.MODE_NIGHT_YES

            viewModel.onDarkModeChanged(isChecked)

            advanceUntilIdle()
            coVerify { mockSaveNightMode.invoke(expectedMode) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onNotificationsSwitchChanged calls saveNotificationsState`() =
        runTest {
            val isChecked = true

            viewModel.onNotificationsSwitchChanged(isChecked)

            advanceUntilIdle()
            coVerify { mockSaveNotificationsState.invoke(isChecked) }
        }
}
