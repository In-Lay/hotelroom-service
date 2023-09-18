package com.inlay.hotelroomservice.presentation.viewmodels.loginregister

import androidx.core.util.PatternsCompat
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.annotations.VisibleForTesting

class AppLoginRegisterViewModel : LoginRegisterViewModel() {
    @VisibleForTesting
    internal val _auth: MutableStateFlow<FirebaseAuth?> = MutableStateFlow(null)
    override val auth = _auth

    private val _userMail = MutableStateFlow("")
    override var userMail = _userMail.asLiveData()

    private val _userPassword = MutableStateFlow("")
    override var userPassword = _userPassword.asLiveData()

    private val _userFullName = MutableStateFlow("")
    override var userFullName = _userFullName.asLiveData()

    private val _supportEmailText = MutableStateFlow("")
    override val supportEmailText = _supportEmailText.asLiveData()

    private val _supportPasswordText = MutableStateFlow("")
    override val supportPasswordText = _supportPasswordText.asLiveData()

    private val _supportFullNameText = MutableStateFlow("")
    override val supportFullNameText = _supportFullNameText.asLiveData()

    private val _toastErrorMessage = MutableStateFlow("")
    override val toastErrorMessage = _toastErrorMessage

    private lateinit var _onClose: () -> Unit
    private lateinit var _onSuggestionClicked: () -> Unit
    private lateinit var _onNavigateToProfile: () -> Unit

    override fun initialize(
        close: () -> Unit,
        onSuggestionClicked: () -> Unit,
        navigateToProfile: () -> Unit,
        auth: FirebaseAuth
    ) {
        _onClose = close
        _onSuggestionClicked = onSuggestionClicked
        _onNavigateToProfile = navigateToProfile

        _auth.value = auth
    }

    override fun close() {
        _onClose()
    }

    override fun onSuggestionClicked() {
        _onSuggestionClicked()
    }

    override fun onEmailTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (!isEmailValid(s.toString())) {
            _supportEmailText.value = "Invalid Email"
        } else {
            _userMail.value = s.toString()
            _supportEmailText.value = ""
        }
    }

    override fun onPasswordTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        _userPassword.value = s.toString()
    }

    override fun onFullNameTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        _userFullName.value = s.toString()
    }

    override fun login() {
        if (_userMail.value.isEmpty()) {
            _supportEmailText.value = "No Email provided"
        } else if (_userPassword.value.isEmpty()) {
            _supportPasswordText.value = "No Password provided"
        } else {
            _auth.value?.signInWithEmailAndPassword(_userMail.value, _userPassword.value)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        _onNavigateToProfile()
                    }
                }?.addOnFailureListener {
                    _toastErrorMessage.value = it.message.toString()
                }
        }
    }

    override fun register() {
        if (_userMail.value.isEmpty()) {
            _supportEmailText.value = "No Email provided"
        } else if (_userPassword.value.isEmpty()) {
            _supportPasswordText.value = "No Password provided"
        } else if (_userFullName.value.isEmpty()) {
            _supportFullNameText.value = "No Full Name provided"
        } else {
            _auth.value?.createUserWithEmailAndPassword(_userMail.value, _userPassword.value)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = _auth.value?.currentUser
                        val builder = UserProfileChangeRequest.Builder()
                        builder.displayName = _userFullName.value

                        user?.updateProfile(builder.build())?.addOnCompleteListener { update ->
                            if (update.isSuccessful) {
                                _onNavigateToProfile()
                            }
                        }
                    }
                }?.addOnFailureListener {
                _toastErrorMessage.value = it.message.toString()
            }
        }
    }

    @VisibleForTesting
    override fun changeUserCredentials(
        userMail: String,
        userPassword: String,
        userFullName: String?
    ) {
        _userMail.value = userMail
        _userPassword.value = userPassword
        if (userFullName != null)
            _userFullName.value = userFullName
    }

    override fun isEmailValid(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }
}