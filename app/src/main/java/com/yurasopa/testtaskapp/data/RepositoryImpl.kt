package com.yurasopa.testtaskapp.data

import com.yurasopa.testtaskapp.data.api.ApiService
import com.yurasopa.testtaskapp.data.remote.User
import com.yurasopa.testtaskapp.data.remote.UserRequest
import com.yurasopa.testtaskapp.data.remote.UserResponse
import com.yurasopa.testtaskapp.domain.Repository
import com.yurasopa.testtaskapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override fun addUser(userRequest: UserRequest): Resource<UserResponse> {
        TODO("Not yet implemented")
    }
}