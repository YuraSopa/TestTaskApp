package com.yurasopa.testtaskapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yurasopa.testtaskapp.data.RepositoryImpl
import com.yurasopa.testtaskapp.data.remote.User
import com.yurasopa.testtaskapp.utils.NetworkConnection
import com.yurasopa.testtaskapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val networkConnection: NetworkConnection
) : ViewModel() {

    private val _hasInternet = MutableStateFlow(networkConnection.isOnline())
    val hasInternet: StateFlow<Boolean> = _hasInternet

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var currentPage = 1
    private var isLastPage = false

    init {
        viewModelScope.launch {
            if (_hasInternet.value) {
                getUsers()
            }
        }
    }

    fun retryConnection() {
        if (networkConnection.isOnline()) {
            _hasInternet.value = true
            getUsers()
        } else {
            _hasInternet.value = false
        }
    }

    fun getUsers() {
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            repository.getUsersList(page = currentPage, count = 6).collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _isLoading.value = false
                    }

                    is Resource.Loading -> {
                        _isLoading.value = true
                    }

                    is Resource.Success -> {

                        resource.data?.let { newUsers ->
                            if (newUsers.isEmpty()) {
                                isLastPage = true
                            } else {
                                _users.value += newUsers
                                currentPage++
                            }
                            _isLoading.value = false
                        } ?: run {
                            isLastPage = true
                        }
                    }
                }
            }
        }
    }
}