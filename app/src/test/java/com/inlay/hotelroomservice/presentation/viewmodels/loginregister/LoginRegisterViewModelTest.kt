package com.inlay.hotelroomservice.presentation.viewmodels.loginregister

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.inlay.hotelroomservice.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse


internal class LoginRegisterViewModelTest {
    class EmailValidator {
        companion object {
            private const val EMAIL_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"
        }

        fun isEmailValid(email: String): Boolean {
            return email.matches(EMAIL_REGEX.toRegex())
        }
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private val emailValidator = mockk<EmailValidator>(relaxed = true)


    private val mockAuth: FirebaseAuth = mockk(relaxed = true)
    private val firebaseUserMock = mockk<FirebaseUser>(relaxed = true)

    private lateinit var viewModel: AppLoginRegisterViewModel

    @Before
    fun setUp() {
        viewModel = AppLoginRegisterViewModel()
    }

    @Test
    fun `test initialize method sets auth value`() {
        assertEquals(mockAuth, viewModel.auth.value)
    }

    @Test
    fun `test isEmailValid returns true for valid email`() {
        val validEmail = "samplemail@gmail.com"

        assertEquals(true, emailValidator.isEmailValid(validEmail))
    }

    @Test
    fun `test isEmailValid returns false for invalid email`() {
        val invalidEmail = "invalid-email"

        val result = emailValidator.isEmailValid(invalidEmail)

        assertFalse(result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test onEmailTextChanged sets supportEmailText correctly`() = runTest {
        val invalidEmail = "invalid-email"

        viewModel.onEmailTextChanged(invalidEmail, 0, 0, invalidEmail.length)

        val observer = Observer<String> {}
        viewModel.supportEmailText.observeForever(observer)
        runCurrent()
        assertEquals("Invalid Email", viewModel.supportEmailText.value)

        viewModel.supportEmailText.removeObserver(observer)
    }


    @Test
    fun `test login method with success navigates to profile`() = runTest {
        val userMail = "email@example.com"
        val userPassword = "password"
        val navigateToProfileMock = mockk<() -> Unit>(relaxed = true)

        viewModel.initialize({}, {}, navigateToProfileMock, mockAuth)

        val mockAuthResult = mockk<AuthResult>(relaxed = true)
        val taskMock = mockk<Task<AuthResult>>(relaxed = true)

        every { taskMock.isSuccessful } returns true
        every { taskMock.result } returns mockAuthResult
        every { mockAuth.signInWithEmailAndPassword(any(), any()) } returns taskMock

        every { taskMock.addOnCompleteListener(any<OnCompleteListener<AuthResult>>()) } answers {
            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(taskMock)
            taskMock
        }

        every { taskMock.addOnFailureListener(any()) } returns taskMock

        viewModel.changeUserCredentials(userMail, userPassword, null)
        viewModel.login()

        verify { navigateToProfileMock.invoke() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test login method with failure shows error message`() = runTest {
        val userMail = "email@example.com"
        val userPassword = "password"

        viewModel._auth.tryEmit(mockAuth)

        val mockAuthResult: AuthResult = mockk(relaxed = true)
        val task: Task<AuthResult> = mockk(relaxed = true)

        every { task.isSuccessful } returns false
        every { task.result } returns mockAuthResult
        every { mockAuth.signInWithEmailAndPassword(any(), any()) } returns task

        every { task.addOnCompleteListener(any<OnCompleteListener<AuthResult>>()) } returns task

        every { task.addOnFailureListener(any()) } answers {
            val listener = arg<OnFailureListener>(0)
            listener.onFailure(Exception("Failure"))
            task
        }

        viewModel.changeUserCredentials(userMail, userPassword, null)
        viewModel.login()

        advanceUntilIdle()
        assertEquals("Failure", viewModel.toastErrorMessage.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test login method with empty email`() = runTest {
        val userMail = ""
        val userPassword = "password"
        val errorText = "No Email provided"

        viewModel.changeUserCredentials(userMail, userPassword, null)
        viewModel.login()

        val observer = Observer<String> {}
        viewModel.supportEmailText.observeForever(observer)
        runCurrent()

        assertEquals(errorText, viewModel.supportEmailText.value)

        viewModel.supportEmailText.removeObserver(observer)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test login method with empty password`() = runTest {
        val userMail = "email@example.com"
        val userPassword = ""
        val errorText = "No Password provided"

        viewModel.changeUserCredentials(userMail, userPassword, null)
        viewModel.login()

        val observer = Observer<String> {}
        viewModel.supportPasswordText.observeForever(observer)
        runCurrent()

        assertEquals(errorText, viewModel.supportPasswordText.value)

        viewModel.supportPasswordText.removeObserver(observer)
    }

    @Test
    fun `test register method with success navigates to profile`() = runTest {
        val userMail = "email@example.com"
        val userPassword = "password"
        val userFullName = "John Doe"

        val navigateToProfileMock = mockk<() -> Unit>(relaxed = true)

        viewModel.initialize({}, {}, navigateToProfileMock, mockAuth)

        val mockAuthResult = mockk<AuthResult>(relaxed = true)
        val taskMock = mockk<Task<AuthResult>>(relaxed = true)

        every { taskMock.isSuccessful } returns true
        every { taskMock.result } returns mockAuthResult
        every { mockAuth.createUserWithEmailAndPassword(any(), any()) } returns taskMock

        every { taskMock.addOnCompleteListener(any<OnCompleteListener<AuthResult>>()) } answers {
            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(taskMock)
            taskMock
        }

        every { taskMock.addOnFailureListener(any()) } returns taskMock

        val profileUpdateBuilder = mockk<UserProfileChangeRequest.Builder>(relaxed = true)

        every { profileUpdateBuilder.displayName } returns userFullName

        val updateTaskMock = mockk<Task<Void>>(relaxed = true)
        val updateTaskResult = mockk<Void>(relaxed = true)

        every { mockAuth.currentUser } returns firebaseUserMock

        every { updateTaskMock.isSuccessful } returns true
        every { updateTaskMock.result } returns updateTaskResult
        every { firebaseUserMock.updateProfile(any()) } returns updateTaskMock

        every { updateTaskMock.addOnCompleteListener(any<OnCompleteListener<Void>>()) } answers {
            val listener = arg<OnCompleteListener<Void>>(0)
            listener.onComplete(updateTaskMock)
            updateTaskMock
        }

        every { updateTaskMock.addOnFailureListener(any()) } returns updateTaskMock

        viewModel.changeUserCredentials(userMail, userPassword, userFullName)

        viewModel.register()

        verify { navigateToProfileMock.invoke() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test register method with failure shows error message`() = runTest {

        val userMail = "email@example.com"
        val userPassword = "password"
        val userFullName = "John Doe"
        viewModel._auth.tryEmit(mockAuth)

        val mockAuthResult: AuthResult = mockk(relaxed = true)

        val task: Task<AuthResult> = mockk(relaxed = true)

        every { task.isSuccessful } returns false
        every { task.result } returns mockAuthResult
        every { mockAuth.createUserWithEmailAndPassword(any(), any()) } returns task

        every { task.addOnCompleteListener(any<OnCompleteListener<AuthResult>>()) } returns task

        every { task.addOnFailureListener(any()) } answers {
            val listener = arg<OnFailureListener>(0)

            listener.onFailure(Exception("Failure"))
            task
        }
        viewModel.changeUserCredentials(userMail, userPassword, userFullName)
        viewModel.register()

        advanceUntilIdle()
        assertEquals("Failure", viewModel.toastErrorMessage.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test register method with empty email sets supportEmailText`() = runTest {
        val email = ""
        val password = "password"
        val name = "John Doe"
        viewModel.changeUserCredentials(email, password, name)

        viewModel.register()

        val stringObserver = Observer<String> {}
        viewModel.supportEmailText.observeForever(stringObserver)

        runCurrent()
        assertEquals("No Email provided", viewModel.supportEmailText.value)

        viewModel.supportEmailText.removeObserver(stringObserver)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test register method with empty password sets supportPasswordText`() = runTest {
        val email = "email@example.com"
        val password = ""
        val name = "John Doe"
        viewModel.changeUserCredentials(email, password, name)

        viewModel.register()

        val stringObserver = Observer<String> {}
        viewModel.supportPasswordText.observeForever(stringObserver)

        runCurrent()
        assertEquals("No Password provided", viewModel.supportPasswordText.value)

        viewModel.supportPasswordText.removeObserver(stringObserver)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test register method with empty password sets supportFullNameText`() = runTest {
        val email = "email@example.com"
        val password = "password"
        val name = ""
        viewModel.changeUserCredentials(email, password, name)

        viewModel.register()

        val stringObserver = Observer<String> {}
        viewModel.supportFullNameText.observeForever(stringObserver)

        runCurrent()
        assertEquals("No Full Name provided", viewModel.supportFullNameText.value)

        viewModel.supportFullNameText.removeObserver(stringObserver)
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
}