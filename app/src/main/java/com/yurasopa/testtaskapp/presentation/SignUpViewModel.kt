package com.yurasopa.testtaskapp.presentation

import android.content.Context
import android.net.Uri
import android.os.Environment.DIRECTORY_PICTURES
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import org.json.JSONException
import org.json.JSONObject
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
) : BaseViewModel(networkConnection) {

    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    private val _registrationResult = MutableStateFlow<Resource<UserResponse>?>(null)
    val registrationResult = _registrationResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError = _nameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError = _phoneError.asStateFlow()

    private val _photoError = MutableStateFlow<String?>(null)
    val photoError = _photoError.asStateFlow()

    private val _generalError = MutableStateFlow<String?>(null)
    val generalError = _generalError.asStateFlow()

    init {
        getToken()
    }

    private fun getToken() {
        viewModelScope.launch {
            when (val result = repository.getToken()) {
                is Resource.Error -> {
                    Timber.d("SignUpViewModel: Error fetching token!")
                }

                is Resource.Loading -> {}
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
        token: String? = _token.value
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            if (hasInternet.value){
                val userRequest = UserRequest(name, email, phone, positionId, photoFile)
                token?.let {
                    repository.addUser(userRequest, it).collect { resource ->
                        when (resource) {
                            is Resource.Error -> {

                                val parsedError = parseErrorBody(resource.error)
                                handleErrorResponse(statusCode = resource.statusCode, parsedError)
                            }

                            is Resource.Loading -> {}

                            is Resource.Success -> {
                                _registrationResult.value = resource
                                clearErrors()
                            }
                        }
                        _isLoading.value = false
                    }
                } ?: run {
                    _generalError.value = "Token is required"
                    _isLoading.value = false
                }
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

    private fun clearErrors() {
        _nameError.value = null
        _emailError.value = null
        _phoneError.value = null
        _photoError.value = null
        _generalError.value = null
    }

    private fun parseErrorBody(error: String?): Map<String, List<String>> {
        return try {
            val jsonObject = JSONObject(error ?: "{}")
            val fails = jsonObject.optJSONObject("fails") ?: JSONObject()
            fails.keys().asSequence().associateWith { key ->
                val errorsArray = fails.optJSONArray(key)
                List(errorsArray?.length() ?: 0) { index -> errorsArray?.getString(index) ?: "" }
            }
        } catch (e: JSONException) {
            emptyMap()
        }
    }


    private fun handleErrorResponse(statusCode: Int?, errorDetails: Map<String, List<String>>) {
        when (statusCode) {
            409 -> {
                _generalError.value = "User with this email or phone already exists."
            }

            422 -> {
                _nameError.value = errorDetails["name"]?.firstOrNull()
                _emailError.value = errorDetails["email"]?.firstOrNull()
                _phoneError.value = errorDetails["phone"]?.firstOrNull()
                _photoError.value = errorDetails["photo"]?.firstOrNull()
            }

            else -> {
                _generalError.value = "An unknown error occurred"
            }
        }
    }

    fun hasErrorsFields(name: String, email: String, phone: String) {
        _nameError.value = if (name.isNotEmpty()) null else "Required field"
        _emailError.value = if (email.isNotEmpty()) null else "Required field"
        _phoneError.value = if (phone.isNotEmpty()) null else "Required field"
        _photoError.value = "Photo is required"
    }
}