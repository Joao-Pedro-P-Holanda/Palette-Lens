package io.github.paletteLens.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.paletteLens.exceptions.AuthenticationException
import io.github.paletteLens.service.auth.AuthService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SignInViewModel.Factory::class)
class
SignInViewModel
    @AssistedInject
    constructor(
        private val authService: AuthService,
        @Assisted("successfulLoginNavigate") private val successfulLoginNavigate: () -> Unit,
        @Assisted("createAccountNavigate") private val createAccountNavigate: () -> Unit,
    ) : ViewModel() {
        private val _email = MutableStateFlow("")
        val email = _email.asStateFlow()
        private val _password = MutableStateFlow("")
        val password = _password.asStateFlow()

        private val _errorState = MutableStateFlow<AuthenticationException?>(null)
        val errorState = _errorState.asStateFlow()

        private val _loadingState = MutableStateFlow(false)
        val loadingState = _loadingState.asStateFlow()

        fun onEmailChange(email: String) {
            _email.value = email
        }

        fun onPasswordChange(password: String) {
            _password.value = password
        }

        fun signInWithEmailAndPassword(
            email: String,
            password: String,
        ) {
            viewModelScope.launch {
                try {
                    authService.signIn(email, password)
                    delay(3000)
                    _errorState.value = null
                } catch (e: AuthenticationException) {
                    _errorState.value = e
                } finally {
                    if (authService.isUserLoggedIn()) {
                        _loadingState.value = false
                        successfulLoginNavigate()
                    }
                }
            }
        }

        fun goToSignUp() {
            viewModelScope.launch {
                createAccountNavigate()
            }
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("successfulLoginNavigate") successfulLoginNavigate: () -> Unit,
                @Assisted("createAccountNavigate") createAccountNavigate: () -> Unit,
            ): SignInViewModel
        }
    }
