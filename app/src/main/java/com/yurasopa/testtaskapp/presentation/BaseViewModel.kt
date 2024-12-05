package com.yurasopa.testtaskapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yurasopa.testtaskapp.utils.NetworkConnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel(
    private val networkConnection: NetworkConnection
) : ViewModel() {

    private val _hasInternet = MutableStateFlow(false)
    val hasInternet = _hasInternet.asStateFlow()

    init {
        observeInternetConnection()
    }

    private fun observeInternetConnection() {
        viewModelScope.launch {
            networkConnection.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    Timber.d("INTERNET: $isOnline $this@BaseViewModel")
                    _hasInternet.value = isOnline
                }
        }
    }
}
