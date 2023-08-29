package com.inlay.hotelroomservice.presentation.viewmodels.loginregister

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.FirebaseAuth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mock
import org.mockito.MockitoAnnotations


internal class LoginRegisterViewModelTest {
    class EmailValidator {
        companion object {
            private const val EMAIL_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"
        }

        fun isEmailValid(email: String): Boolean {
            return email.matches(EMAIL_REGEX.toRegex())
        }
    }

    //    @Test
//    fun login() {
//    }
//
////    @org.junit.jupiter.api.Test
//    @Test
//    fun register() {
//    }
//
//    @Test
//    fun isEmailValid() {
//    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val emailValidator = mockk<EmailValidator>()

    @Mock
    private lateinit var mockAuth: FirebaseAuth

    private lateinit var viewModel: AppLoginRegisterViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mockAuth = mockk()
        viewModel = spyk(AppLoginRegisterViewModel())
        viewModel.initialize({}, {}, {}, mockAuth)
    }

    @Test
    fun `test initialize method sets auth value`() {
        assertEquals(mockAuth, viewModel.auth.value)
    }

    @Test
    fun `test MutableLiveData fields are initialized properly`() {
        assertEquals("", viewModel.userMail.value)
        assertEquals("", viewModel.userPassword.value)
        assertEquals("", viewModel.userFullName.value)
        assertEquals("", viewModel.toastErrorMessage.value)
        assertEquals("", viewModel.supportPasswordText.value)
        // Add similar assertions for other fields
    }

//    @Test
//    fun `test onEmailTextChanged sets supportEmailText correctly`() {
////        mockkStatic(Patterns::class)
////        every { Patterns.EMAIL_ADDRESS.matcher(any()) } returns mockk {
////            every { matches() } returns true
////        }
//        every { emailValidator.isEmailValid(any()) } returns false
//
//        viewModel.onEmailTextChanged("invalid-email", 0, 0, 0)
//        assertEquals("Invalid Email", viewModel.supportEmailText.value)
//
//        every { emailValidator.isEmailValid(any()) } returns true
//
//        viewModel.onEmailTextChanged("valid@email.com", 0, 0, 0)
//        assertEquals("", viewModel.supportEmailText.value)
//
////        unmockkStatic(Patterns::class)
//    }

    @Test
    fun `test register method with failure shows error message`() {
        val userMail = "email@example.com"
        val userPassword = "password"
        val userFullName = "John Doe"

        every { viewModel.userMail.value } returns userMail
        every { viewModel.userPassword.value } returns userPassword
        every { viewModel.userFullName.value } returns userFullName

        coEvery { mockAuth.createUserWithEmailAndPassword(userMail, userPassword) } returns mockk {
            every { isSuccessful } returns false
            every { exception } returns mockk(relaxed = true) {
                every { message } returns "Registration failed"
            }
        }

        viewModel.register()

        coVerify { mockAuth.createUserWithEmailAndPassword(userMail, userPassword) }
        assertEquals("Registration failed", viewModel.toastErrorMessage.value)
    }

    @Test
    fun `test register method with empty password sets supportPasswordText`() {
        every { viewModel.userMail.value } returns "email@example.com"
        every { viewModel.userFullName.value } returns "John Doe"

        viewModel.register()

        assertEquals("No Password provided", viewModel.supportPasswordText.value)
    }

    @Test
    fun `test close method invokes onClose`() {
        val onCloseMock = mockk<() -> Unit>(relaxed = true)
        viewModel.initialize(close = onCloseMock, {}, {}, mockAuth)

        viewModel.close()

        verify { onCloseMock.invoke() }
    }

    @Test
    fun `test SuggestionClicked invokes onSuggestionClicked`() {
        val onSuggestionClickedMockk = mockk<() -> Unit>(relaxed = true)
        viewModel.initialize({}, onSuggestionClickedMockk, {}, mockAuth)

        viewModel.onSuggestionClicked()

        verify { onSuggestionClickedMockk.invoke() }
    }

    @Test
    fun `test isEmailValid returns true for valid email`() {
        val emailValidator = EmailValidator()
        val validEmail = "email@example.com"

        val result = emailValidator.isEmailValid(validEmail)

        assertTrue(result)
    }

    @Test
    fun `test isEmailValid returns false for invalid email`() {
        val emailValidator = EmailValidator()
        val invalidEmail = "invalid-email"

        val result = emailValidator.isEmailValid(invalidEmail)

        assertFalse(result)
    }
}