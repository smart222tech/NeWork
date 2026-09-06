package ru.netology.nmedia.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val apiService: ApiService
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkAuth()
    }

    fun login(login: String, pass: String) {
        viewModelScope.launch {
            try {
                val response = apiService.login(login, pass)
                if (response.isSuccessful) {
                    val token = response.body()
                    if (token != null) {
                        tokenStorage.saveAuth(token.id, token.token)
                        _authState.value = AuthState.Authenticated(token.id, token.token)
                    }
                } else {
                    _authState.value = AuthState.Error("Неправильный логин или пароль")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Ошибка сети")
            }
        }
    }

    fun register(login: String, pass: String, name: String, avatar: MultipartBody.Part? = null) {
        viewModelScope.launch {
            try {
                val response = apiService.register(
                    MultipartBody.Part.createFormData("login", login),
                    MultipartBody.Part.createFormData("password", pass),
                    MultipartBody.Part.createFormData("name", name),
                    avatar
                )
                if (response.isSuccessful) {
                    val token = response.body()
                    if (token != null) {
                        tokenStorage.saveAuth(token.id, token.token)
                        _authState.value = AuthState.Authenticated(token.id, token.token)
                    }
                } else if (response.code() == 400) {
                    _authState.value = AuthState.Error("Пользователь с таким логином уже зарегистрирован")
                } else {
                    _authState.value = AuthState.Error("Ошибка регистрации")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Ошибка сети")
            }
        }
    }

    fun logout() {
        tokenStorage.clearToken()
        _authState.value = AuthState.Unauthenticated
    }

    fun checkAuth() {
        val token = tokenStorage.getToken()
        val id = tokenStorage.getUserId()
        if (token != null) {
            _authState.value = AuthState.Authenticated(id, token)
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }
}

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val id: Long, val token: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
