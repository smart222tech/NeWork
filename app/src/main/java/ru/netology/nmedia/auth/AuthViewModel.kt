package ru.netology.nmedia.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tokenStorage: TokenStorage
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    fun login(token: String) {
        tokenStorage.saveToken(token)
        _authState.value = AuthState.Authenticated(token)
    }

    fun logout() {
        tokenStorage.clearToken()
        _authState.value = AuthState.Unauthenticated
    }

    fun checkAuth() {
        val token = tokenStorage.getToken()
        if (token != null) {
            _authState.value = AuthState.Authenticated(token)
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }
}

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val token: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
