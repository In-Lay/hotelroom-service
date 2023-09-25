package com.inlay.hotelroomservice.presentation.viewmodels.profile

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseUser
import com.inlay.hotelroomservice.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


internal class AppProfileViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: AppProfileViewModel

    @Before
    fun setUp() {
        viewModel = AppProfileViewModel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun initializeData() = runTest {
        val logoutMock = mockk<() -> Unit>(relaxed = true)
        val editMock = mockk<() -> Unit>(relaxed = true)
        val firebaseUser = mockk<FirebaseUser>(relaxed = true)

        every { firebaseUser.displayName } returns "User Name"
        every { firebaseUser.email } returns "sampleMail@mail.com"
        every { firebaseUser.photoUrl } returns Uri.fromParts("scheme", "ssp", "fragment")

        viewModel.initializeData(logoutMock, editMock, firebaseUser)

        val observer = Observer<String> {}
        val uriObserver = Observer<Uri?> {}
        viewModel.apply {
            userName.observeForever(observer)
            userEmail.observeForever(observer)
            profileImage.observeForever(uriObserver)
        }

        runCurrent()

        assertEquals("User Name", viewModel.userName.value)
        assertEquals("sampleMail@mail.com", viewModel.userEmail.value)
        assertEquals(Uri.fromParts("scheme", "ssp", "fragment"), viewModel.profileImage.value)

        viewModel.apply {
            profileImage.removeObserver(uriObserver)
            userEmail.removeObserver(observer)
            userName.removeObserver(observer)
        }
    }

    @Test
    fun logout() {
        val logoutMock = mockk<() -> Unit>(relaxed = true)

        viewModel.initializeData(logoutMock, {}, null)
        viewModel.logout()

        verify { logoutMock.invoke() }
    }

    @Test
    fun edit() {
        val editMock = mockk<() -> Unit>(relaxed = true)

        viewModel.initializeData({}, editMock, null)
        viewModel.edit()

        verify { editMock.invoke() }
    }
}