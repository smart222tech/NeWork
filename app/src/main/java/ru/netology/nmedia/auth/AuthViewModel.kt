package ru.netology.nmedia.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.api.ApiService
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
                        _authState.value = AuthState.Authenticated(
                            token.id,
                            token.token
                        )
                    } else {
                        Log.e("AuthDebug", "Login returned an empty response body")
                        _authState.value = AuthState.Error(
                            "Сервер вернул пустой ответ при входе"
                        )
                    }
                } else {
                    Log.e(
                        "AuthDebug",
                        "Login HTTP error: code=${response.code()}"
                    )

                    _authState.value = AuthState.Error(
                        if (response.code() == 400) {
                            "Неправильный логин или пароль"
                        } else {
                            "Ошибка входа: HTTP ${response.code()}"
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("AuthDebug", "Login request failed", e)
                _authState.value = AuthState.Error(
                    "Ошибка входа: ${e.javaClass.simpleName}"
                )
            }
        }
    }

    fun register(
        login: String,
        pass: String,
        name: String,
        avatar: MultipartBody.Part? = null
    ) {
        viewModelScope.launch {
            try {
                val textPlainMediaType = "text/plain".toMediaType()

                val response = apiService.register(
                    login.toRequestBody(textPlainMediaType),
                    pass.toRequestBody(textPlainMediaType),
                    name.toRequestBody(textPlainMediaType),
                    avatar
                )

                if (response.isSuccessful) {
                    val token = response.body()

                    if (token != null) {
                        tokenStorage.saveAuth(token.id, token.token)
                        _authState.value = AuthState.Authenticated(
                            token.id,
                            token.token
                        )
                    } else {
                        Log.e(
                            "AuthDebug",
                            "Registration returned an empty response body"
                        )
                        _authState.value = AuthState.Error(
                            "Сервер вернул пустой ответ при регистрации"
                        )
                    }
                } else {
                    Log.e(
                        "AuthDebug",
                        "Registration HTTP error: code=${response.code()}"
                    )

                    _authState.value = AuthState.Error(
                        when (response.code()) {
                            400 -> "Пользователь с таким логином уже зарегистрирован"
                            else -> "Ошибка регистрации: HTTP ${response.code()}"
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("AuthDebug", "Registration request failed", e)
                _authState.value = AuthState.Error(
                    "Ошибка регистрации: ${e.javaClass.simpleName}"
                )
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
