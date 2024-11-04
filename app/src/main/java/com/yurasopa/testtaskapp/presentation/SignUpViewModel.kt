package com.yurasopa.testtaskapp.presentation

import android.content.Context
import android.net.Uri
import android.os.Environment.DIRECTORY_PICTURES
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yurasopa.testtaskapp.data.RepositoryImpl
import com.yurasopa.testtaskapp.data.remote.UserRequest
import com.yurasopa.testtaskapp.data.remote.UserResponse
import com.yurasopa.testtaskapp.utils.NetworkConnection
import com.yurasopa.testtaskapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val networkConnection: NetworkConnection
) : ViewModel() {

    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    private val _registrationResult = MutableStateFlow<Resource<UserResponse>?>(null)
    val registrationResult = _registrationResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        getToken()
    }

    private fun getToken(){
        viewModelScope.launch {
            when(val result = repository.getToken()){
                is Resource.Error -> { Timber.d("SignUpViewModel: Error fetching token!") }
                is Resource.Loading -> {  }
                is Resource.Success -> {
                    _token.value = result.data
                }
            }
        }
    }

    fun clearRegistrationResult() {
        _registrationResult.value = null
    }

    fun addUser(
        name: String,
        email: String,
        phone: String,
        positionId: Int,
        photoFile: File,
        token: String? =_token.value
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            val userRequest = UserRequest(name, email, phone, positionId, photoFile)
            token?.let {
                repository.addUser(userRequest, it).collect { resource ->
                    when(resource){
                        is Resource.Error -> {
                            _errorMessage.value = resource.error ?: "Unknown error occurred"
                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            _registrationResult.value = resource
                            _errorMessage.value = null
                        }
                    }
                    _isLoading.value = false
                }
            } ?: run {
                _errorMessage.value = "Token is required"
                _isLoading.value = false
            }
        }
    }


    fun createImageUri(context: Context, cameraImageUri: MutableState<Uri?>): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir =
            ContextCompat.getExternalFilesDirs(context, DIRECTORY_PICTURES)
                .first()

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            File.createTempFile("IMAGE_$timeStamp", ".jpg", storageDir).apply {
                cameraImageUri.value = Uri.fromFile(this)
            }
        )
    }
}