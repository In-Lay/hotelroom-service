package com.inlay.hotelroomservice.presentation.viewmodels.editprofile

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.inlay.hotelroomservice.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class AppEditProfileViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: AppEditProfileViewModel

    private val firebaseUserMock = mockk<FirebaseUser>(relaxed = true)

    @Before
    fun setUp() {
        val getAuthUserMock = mockk<GetAuthUser>(relaxed = true)
        every { getAuthUserMock.invoke() } returns firebaseUserMock

        viewModel = AppEditProfileViewModel(getAuthUserMock)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun initialize() = runTest {
        val userPhotoMock = mockk<Uri>(relaxed = true)

        every { firebaseUserMock.displayName } returns "User Name"
        every { firebaseUserMock.email } returns "userMail@mail.com"
        every { firebaseUserMock.photoUrl } returns userPhotoMock

        viewModel.initialize(firebaseUserMock, {}, {})

        advanceUntilIdle()
        assertEquals(firebaseUserMock, viewModel.user.value)

        val uriObserver = Observer<Uri?> {}
        val stringObserver = Observer<String> {}

        viewModel.apply {
            email.observeForever(stringObserver)
            fullName.observeForever(stringObserver)
            userPhoto.observeForever(uriObserver)
        }

        runCurrent()
        assertEquals(userPhotoMock, viewModel.userPhoto.value)
        assertEquals("User Name", viewModel.fullName.value)
        assertEquals("userMail@mail.com", viewModel.email.value)

        viewModel.apply {
            email.removeObserver(stringObserver)
            fullName.removeObserver(stringObserver)
            userPhoto.removeObserver(uriObserver)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onFullNameChanged() = runTest {
        val nameText: CharSequence = "User name"

        viewModel.onFullNameChanged(nameText, 0, 0, nameText.length)

        advanceUntilIdle()
        assertEquals(true, viewModel.fullNameChanged.value)
        assertEquals(false, viewModel.changesApplied.value)

        val observer = Observer<String> {}
        viewModel.fullName.observeForever(observer)

        runCurrent()
        assertEquals(nameText.toString(), viewModel.fullName.value)

        viewModel.fullName.removeObserver(observer)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEmailChanged with invalid email`() = runTest {
        val invalidEmail: CharSequence = "Invalid Email"

        viewModel.onEmailChanged(invalidEmail, 0, 0, invalidEmail.length)

        advanceUntilIdle()
        assertEquals(true, viewModel.emailChanged.value)
        assertEquals(false, viewModel.changesApplied.value)

        val observer = Observer<String> { }
        viewModel.supportEmailText.observeForever(observer)

        runCurrent()
        assertEquals("Invalid Email", viewModel.supportEmailText.value)

        viewModel.supportEmailText.removeObserver(observer)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEmailChanged with valid email`() = runTest {
        val invalidEmail: CharSequence = "userMail@mail.com"

        viewModel.onEmailChanged(invalidEmail, 0, 0, invalidEmail.length)

        advanceUntilIdle()
        assertEquals(true, viewModel.emailChanged.value)
        assertEquals(false, viewModel.changesApplied.value)

        val observer = Observer<String> { }
        viewModel.apply {
            email.observeForever(observer)
            supportEmailText.observeForever(observer)
        }

        runCurrent()
        assertEquals("userMail@mail.com", viewModel.email.value)
        assertEquals("", viewModel.supportEmailText.value)

        viewModel.apply {
            email.removeObserver(observer)
            supportEmailText.removeObserver(observer)
        }
    }

    @Test
    fun changePhoto() {
        val showPhotoPickerMock = mockk<() -> Unit>(relaxed = true)

        viewModel.initialize(null, {}, showPhotoPickerMock)
        viewModel.changePhoto()

        verify { showPhotoPickerMock.invoke() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `save with applied changes`() = runTest {
        val toastMsg = "You have changed nothing!"

        viewModel.initialize(null, {}, {})
        viewModel.editChangesState(
            isEmailChanged = false, isFullNameChanged = false, isChangesApplied = true
        )
        viewModel.save()

        advanceUntilIdle()
        assertEquals(toastMsg, viewModel.toastText.value)
    }


    @Test
    fun `save with unapplied changes`() {
        val showAuthDialogMock = mockk<() -> Unit>(relaxed = true)

        viewModel.apply {
            initialize(null, showAuthDialogMock) {}
            editChangesState(
                isEmailChanged = true, isFullNameChanged = true, isChangesApplied = false
            )
            save()
        }

        changeFullName()
        verify { showAuthDialogMock.invoke() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun changeFullName() = runTest {
        println("changeFullName test start")
        val profileUpdateBuilder = mockk<UserProfileChangeRequest.Builder>(relaxed = true)

        every { profileUpdateBuilder.displayName } returns "New User Name"

        val updateTaskMock = mockk<Task<Void>>(relaxed = true)
        val updateTaskResultMock = mockk<Void>(relaxed = true)

        every { updateTaskMock.isSuccessful } returns true
        every { updateTaskMock.result } returns updateTaskResultMock

        every { firebaseUserMock.updateProfile(any()) } returns updateTaskMock

        every { updateTaskMock.addOnCompleteListener(any<OnCompleteListener<Void>>()) } answers {
            val listener = arg<OnCompleteListener<Void>>(0)
            listener.onComplete(updateTaskMock)
            updateTaskMock
        }

        every { updateTaskMock.addOnFailureListener(any()) } returns updateTaskMock


        viewModel.changeUserData(firebaseUserMock, "", "New User Name")
        viewModel.changeFullName()

        advanceUntilIdle()
        assertEquals(true, viewModel.changesApplied.value)
        assertEquals("Changes applied", viewModel.toastText.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `changeEmail with successful auth`() = runTest {
        println("start of the test")
        val userMail = "userMail@mail.com"
        val userPassword = "password"

        val authTaskMock = mockk<Task<Void>>(relaxed = true)
        val authTaskResultMock = mockk<Void>(relaxed = true)

        every { authTaskMock.isSuccessful } returns true
        every { authTaskMock.result } returns authTaskResultMock

        every { firebaseUserMock.reauthenticate(any()) } returns authTaskMock

        every { authTaskMock.addOnCompleteListener(any<OnCompleteListener<Void>>()) } answers {
            val listener = arg<OnCompleteListener<Void>>(0)
            listener.onComplete(authTaskMock)
            authTaskMock
        }
        every { authTaskMock.addOnFailureListener(any()) } returns authTaskMock

        val updateEmailTaskMock = mockk<Task<Void>>(relaxed = true)
        val updateEmailTaskResult = mockk<Void>(relaxed = true)

        every { updateEmailTaskMock.isSuccessful } returns true
        every { updateEmailTaskMock.result } returns updateEmailTaskResult

        every { firebaseUserMock.updateEmail(any()) } returns updateEmailTaskMock

        every { updateEmailTaskMock.addOnCompleteListener(any<OnCompleteListener<Void>>()) } answers {
            val listener = arg<OnCompleteListener<Void>>(0)
            listener.onComplete(updateEmailTaskMock)
            updateEmailTaskMock
        }
        every { updateEmailTaskMock.addOnFailureListener(any()) } returns updateEmailTaskMock

        viewModel.changeUserData(firebaseUserMock, userMail, "")
        viewModel.changeEmail(userMail, userPassword)

        advanceUntilIdle()
        assertEquals("Changes applied", viewModel.toastText.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `changeEmail with failed auth`() = runTest {
        val userMail = "userMail@mail.com"
        val userPassword = "password"

        val authTaskMock = mockk<Task<Void>>(relaxed = true)
        val authTaskResultMock = mockk<Void>(relaxed = true)

        every { authTaskMock.isSuccessful } returns false
        every { authTaskMock.result } returns authTaskResultMock

        every { firebaseUserMock.reauthenticate(any()) } returns authTaskMock

        every { authTaskMock.addOnCompleteListener(any<OnCompleteListener<Void>>()) } returns authTaskMock
        every { authTaskMock.addOnFailureListener(any()) } answers {
            val listener = arg<OnFailureListener>(0)
            listener.onFailure(Exception("Failure"))
            authTaskMock
        }

        viewModel.changeUserData(firebaseUserMock, userMail, "")
        viewModel.changeEmail(userMail, userPassword)

        advanceUntilIdle()
        assertEquals("Failure", viewModel.toastText.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testChangePhoto() = runTest {
        val localPhotoUriMock = mockk<Uri>(relaxed = true)
        val remotePhotoUriMock = mockk<Uri>(relaxed = true)
        val downloadUrlMock = mockk<Task<Uri>>(relaxed = true)

        val fireStoreReferenceMock = mockk<StorageReference>(relaxed = true)

        val uploadTaskMock = mockk<UploadTask>(relaxed = true)
        val uploadTaskResultMock = mockk<UploadTask.TaskSnapshot>(relaxed = true)

        every { fireStoreReferenceMock.child(any()) } returns fireStoreReferenceMock
        every { fireStoreReferenceMock.putFile(any()) } returns uploadTaskMock

        every { uploadTaskMock.isSuccessful } returns true
        every { uploadTaskMock.result } returns uploadTaskResultMock
        every { fireStoreReferenceMock.downloadUrl } returns downloadUrlMock

        val taskUriMock = mockk<Task<Uri>>(relaxed = true)

        every { uploadTaskMock.continueWithTask(any<Continuation<UploadTask.TaskSnapshot, Task<Uri>>>()) } returns taskUriMock

        every { taskUriMock.isSuccessful } returns true
        every { taskUriMock.result } returns remotePhotoUriMock

        every { taskUriMock.addOnCompleteListener(any<OnCompleteListener<Uri>>()) } answers {
            val listener = arg<OnCompleteListener<Uri>>(0)
            listener.onComplete(taskUriMock)
            taskUriMock
        }
        every { taskUriMock.addOnFailureListener(any()) } returns taskUriMock

        val profileUpdateBuilderMock = mockk<UserProfileChangeRequest.Builder>(relaxed = true)

        every { profileUpdateBuilderMock.photoUri } returns remotePhotoUriMock

        val updateTaskMock = mockk<Task<Void>>(relaxed = true)
        val updateTaskResultMock = mockk<Void>(relaxed = true)

        every { updateTaskMock.isSuccessful } returns true
        every { updateTaskMock.result } returns updateTaskResultMock

        every { firebaseUserMock.updateProfile(any()) } returns updateTaskMock

        every { updateTaskMock.addOnCompleteListener(any<OnCompleteListener<Void>>()) } answers {
            val listener = arg<OnCompleteListener<Void>>(0)
            listener.onComplete(updateTaskMock)
            updateTaskMock
        }
        every { updateTaskMock.addOnFailureListener(any()) } returns updateTaskMock

        viewModel.changeUserData(firebaseUserMock, "", "")
        viewModel.changePhoto(localPhotoUriMock, fireStoreReferenceMock)


        val uriObserver = Observer<Uri?> {}
        viewModel.userPhoto.observeForever(uriObserver)

        runCurrent()
        assertEquals(localPhotoUriMock, viewModel.userPhoto.value)

        advanceUntilIdle()
        assertEquals(true, viewModel.changesApplied.value)
        assertEquals("Changes applied", viewModel.toastText.value)

        viewModel.userPhoto.removeObserver(uriObserver)
    }

    @Test
    fun `isEmailValid with invalid email`() {
        val invalidEmail = "invalid-email"

        val result = viewModel.isEmailValid(invalidEmail)

        assertEquals(false, result)
    }

    @Test
    fun `isEmailValid with valid email`() {
        val validEmail = "sampleMail@mail.com"

        val result = viewModel.isEmailValid(validEmail)

        assertEquals(true, result)
    }
}