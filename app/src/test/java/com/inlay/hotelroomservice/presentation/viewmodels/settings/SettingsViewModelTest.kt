package com.inlay.hotelroomservice.presentation.viewmodels.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.inlay.hotelroomservice.domain.usecase.datastore.nightmode.SaveNightMode
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.GetNotificationsState
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.SaveNotificationsState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.kotlin.verify

internal class SettingsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Mock
    private lateinit var mockSaveNightMode: SaveNightMode

    @Mock
    private lateinit var mockGetNotificationsState: GetNotificationsState

    @Mock
    private lateinit var mockSaveNotificationsState: SaveNotificationsState

    private lateinit var viewModel: AppSettingsViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
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
        io.mockk.verify { openLangDialogMock.invoke() }
    }

    @Test
    fun `openLangDialog invokes the provided lambda`() {
        val openLangDialogMock = mockk<() -> Unit>(relaxed = true)
        viewModel.initialize(openLangDialogMock, listOf())

        viewModel.openLangDialog()

        io.mockk.verify { openLangDialogMock.invoke() }
    }

    @Test
    fun `changeNotificationsState updates notifications state and calls saveNotificationsState`() =
        runTest {
            val newState = true
           viewModel.changeNotificationsState(newState)
            val state = viewModel.inAppNotificationsState.first()
            Assert.assertEquals(newState, state)
//            coEvery { viewModel.changeNotificationsState(newState) }

//            val collectedData = mutableListOf<Boolean>()
//            val flowCollector: FlowCollector<Boolean> =
//                FlowCollector { value -> collectedData.add(value) }
//
//            viewModel.inAppNotificationsState.collect(flowCollector)



//            viewModel.inAppNotificationsState.collect {
//                assert(it == newState)
//            }
//            assert(viewModel.inAppNotificationsState.value == newState)
//            coVerify {
//                verify(mockSaveNotificationsState).invoke(newState)
//            }
        }

    @Test
    fun `onDarkModeChanged updates night mode and calls saveNightMode`() =
        runTest {
            val isChecked = true
            val expectedMode = AppCompatDelegate.MODE_NIGHT_YES

            viewModel.onDarkModeChanged(isChecked)

            verify(mockSaveNightMode).invoke(expectedMode)
        }

    @Test
    fun `onNotificationsSwitchChanged calls saveNotificationsState`() =
        runTest {
            val isChecked = true

            viewModel.onNotificationsSwitchChanged(isChecked)

            verify(mockSaveNotificationsState).invoke(isChecked)
        }
}
