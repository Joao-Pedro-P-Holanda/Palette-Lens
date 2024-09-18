package io.github.paletteLens.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.paletteLens.exceptions.AuthenticationException
import io.github.paletteLens.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val authService: AuthService,
    ) : ViewModel() {
        private val _errorState = MutableStateFlow<AuthenticationException?>(null)
        val errorState = _errorState.asStateFlow()
        private val _loadingState = MutableStateFlow(false)
        val loadingState = _loadingState.asStateFlow()
        private val _successState = MutableStateFlow(false)
        val successState = _successState.asStateFlow()

        fun signUpWithEmailAndPassword(
            email: String,
            password: String,
        ) {
            _loadingState.value = true
            viewModelScope.launch {
                try {
                    authService.signUp(email, password)
                    _successState.value = true
                } catch (e: AuthenticationException) {
                    _errorState.value = e
                } finally {
                    _loadingState.value = false
                }
            }
        }
    }
