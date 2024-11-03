package com.yurasopa.testtaskapp.data.api

import com.yurasopa.testtaskapp.data.remote.UserRequest
import com.yurasopa.testtaskapp.data.remote.UsersResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("page")
        page: Int,
        @Query("count")
        count: Int,
    ): Response<UsersResponse>

    @POST("users")
    suspend fun addUser(
        @Body
        user: UserRequest
    ): Response<UsersResponse>

    companion object {
        const val BASE_URL = "https://frontend-test-assignment-api.abz.agency/api/v1/"

        private val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()
    }
}