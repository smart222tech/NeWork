package ru.netology.nmedia.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.api.dto.Job
import ru.netology.nmedia.api.dto.Post
import ru.netology.nmedia.api.dto.User
import ru.netology.nmedia.repository.UserRepository
import ru.netology.nmedia.repository.JobRepository
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository,
    private val api: ApiService // Still need direct API for some calls like getUserWall
) : ViewModel() {
    val data = userRepository.data.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _userWall = MutableStateFlow<List<Post>>(emptyList())
    val userWall: StateFlow<List<Post>> = _userWall

    private val _userJobs = MutableStateFlow<List<Job>>(emptyList())
    val userJobs: StateFlow<List<Job>> = _userJobs

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        userRepository.getAll()
    }

    fun getUserDetails(userId: Long) = viewModelScope.launch {
        try {
            _userWall.value = api.getUserWall(userId)
            jobRepository.getJobs(userId)
            // Note: JobRepository updates its own flow, but here we want to capture it for details
            _userJobs.value = api.getJobs(userId)
        } catch (e: Exception) {}
    }

    fun saveJob(job: Job) = viewModelScope.launch {
        try {
            jobRepository.saveJob(job)
        } catch (e: Exception) {}
    }

    fun deleteJob(id: Long) = viewModelScope.launch {
        try {
            jobRepository.deleteJob(id)
            _userJobs.value = _userJobs.value.filter { it.id != id }
        } catch (e: Exception) {}
    }
}
