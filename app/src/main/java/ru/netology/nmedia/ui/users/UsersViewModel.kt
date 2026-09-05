package ru.netology.nmedia.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.api.dto.User
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    private val _data = MutableStateFlow<List<User>>(emptyList())
    val data: StateFlow<List<User>> = _data

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        try {
            _data.value = api.getUsers()
        } catch (e: Exception) {}
    }
}
