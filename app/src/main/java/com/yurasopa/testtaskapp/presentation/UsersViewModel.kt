package com.yurasopa.testtaskapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yurasopa.testtaskapp.data.RepositoryImpl
import com.yurasopa.testtaskapp.data.remote.User
import com.yurasopa.testtaskapp.utils.NetworkConnection
import com.yurasopa.testtaskapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val networkConnection: NetworkConnection
) : ViewModel() {

    private val hasInternet: Boolean
        get() = networkConnection.isOnline()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()


    init {
        viewModelScope.launch {
            if (hasInternet) {
                getUsers()
            }
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            repository.getUsersList(page = 1, count = 6).collect { resource ->
                when (resource) {
                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        _users.value = resource.data ?: emptyList()
                    }
                }
            }

        }
    }
}