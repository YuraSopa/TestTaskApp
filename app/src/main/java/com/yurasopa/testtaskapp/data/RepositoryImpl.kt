package com.yurasopa.testtaskapp.data

import com.yurasopa.testtaskapp.data.api.ApiService
import com.yurasopa.testtaskapp.data.remote.User
import com.yurasopa.testtaskapp.data.remote.UserRequest
import com.yurasopa.testtaskapp.data.remote.UserResponse
import com.yurasopa.testtaskapp.domain.Repository
import com.yurasopa.testtaskapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: ApiService
) : Repository {
    override fun getUsersList(page: Int, count: Int): Flow<Resource<List<User>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getUsers(page, count)
                if (response.isSuccessful) {
                    val users = response.body()?.users
                    if (users != null) {
                        emit(Resource.Success(users))
                    } else {
                        emit(Resource.Error("No users found"))
                    }
                } else {
                    emit(Resource.Error(response.message()))
                }

            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }

        }
    }

    override fun addUser(userRequest: UserRequest, token: String): Flow<Resource<UserResponse>> {
        return flow {
            emit(Resource.Loading())
            try {

                val photoPart = MultipartBody.Part.createFormData(
                    "photo",
                    userRequest.photo.name,
                    userRequest.photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )

                val response = api.addUser(
                    token = token,
                    name = userRequest.name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    email = userRequest.email.toRequestBody("text/plain".toMediaTypeOrNull()),
                    phone = userRequest.phone.toRequestBody("text/plain".toMediaTypeOrNull()),
                    positionId = userRequest.positionId.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull()),
                    photo = photoPart
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    } ?: emit(Resource.Error("Empty response body"))
                } else {
                    emit(Resource.Error(response.message()))
                }

            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override suspend fun getToken(): Resource<String> {
        return try {
            val response = api.getToken()
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (token != null) {
                    Resource.Success(token)
                } else {
                    Resource.Error("Token not found")
                }
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}
