package com.yurasopa.testtaskapp.presentation

import androidx.lifecycle.viewModelScope
import com.yurasopa.testtaskapp.system.notifier.AppNotifier
import com.yurasopa.testtaskapp.system.notifier.RetryConnectionEvent
import com.yurasopa.testtaskapp.utils.NetworkConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkConnection: NetworkConnection,
    private val notifier: AppNotifier
) : BaseViewModel(networkConnection) {

    fun sendEvent(retryConnectionEvent: RetryConnectionEvent) {
        viewModelScope.launch {
            notifier.send(retryConnectionEvent)
        }
    }
}