package com.yurasopa.testtaskapp.domain

import com.yurasopa.testtaskapp.data.remote.User
import com.yurasopa.testtaskapp.data.remote.UserRequest
import com.yurasopa.testtaskapp.data.remote.UserResponse
import com.yurasopa.testtaskapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getUsersList(page: Int, count: Int): Flow<Resource<List<User>>>
    fun addUser(userRequest: UserRequest): Resource<UserResponse>
}