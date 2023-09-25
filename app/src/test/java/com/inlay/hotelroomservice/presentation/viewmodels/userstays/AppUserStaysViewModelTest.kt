package com.inlay.hotelroomservice.presentation.viewmodels.userstays

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseUser
import com.inlay.hotelroomservice.CoroutineTestRule
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


internal class AppUserStaysViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private val firebaseUser = mockk<FirebaseUser>(relaxed = true)

    private lateinit var viewModel: AppUserStaysViewModel

    @Before
    fun setUp() {
        viewModel = AppUserStaysViewModel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun initializeData() = runTest {
        val goToHotelsMock = mockk<() -> Unit>(relaxed = true)
        val goToProfileMock = mockk<() -> Unit>(relaxed = true)

        every { firebaseUser.displayName } returns "Display Name"
        every { firebaseUser.photoUrl } returns Uri.fromParts("scheme", "ssp", "fragment")

        viewModel.initializeData(goToHotelsMock, goToProfileMock, true, firebaseUser, "Day", "")


        advanceUntilIdle()
        assertEquals(firebaseUser, viewModel.user.value)
        assertEquals(true, viewModel.isUserLogged.value)

        val stringObserver = Observer<String> {}
        val uriObserver = Observer<Uri?> {}

        viewModel.apply {
            profileHeaderText.observeForever(stringObserver)
            profileUsernameText.observeForever(stringObserver)
            profileImage.observeForever(uriObserver)
        }
        runCurrent()

        assertEquals("Day", viewModel.profileHeaderText.value)
        assertEquals("Display Name!", viewModel.profileUsernameText.value)
        assertEquals(Uri.fromParts("scheme", "ssp", "fragment"), viewModel.profileImage.value)

        viewModel.apply {
            profileHeaderText.removeObserver(stringObserver)
            profileUsernameText.removeObserver(stringObserver)
            profileImage.removeObserver(uriObserver)
        }
    }

    @Test
    fun goToHotels() {
        val goToHotelsMock = mockk<() -> Unit>(relaxed = true)

        viewModel.initializeData(goToHotelsMock, {}, false, null, "Day", "")
        viewModel.goToHotels()

        verify { goToHotelsMock.invoke() }
    }

    @Test
    fun goToProfile() {
        val goToProfileMock = mockk<() -> Unit>(relaxed = true)

        viewModel.initializeData({}, goToProfileMock, true, firebaseUser, "Day", "")
        viewModel.goToProfile()

        verify { goToProfileMock.invoke() }
    }
}