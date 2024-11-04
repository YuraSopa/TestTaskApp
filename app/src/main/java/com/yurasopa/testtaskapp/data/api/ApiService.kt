package com.yurasopa.testtaskapp.data.api

import com.yurasopa.testtaskapp.data.remote.TokenResponse
import com.yurasopa.testtaskapp.data.remote.UserResponse
import com.yurasopa.testtaskapp.data.remote.UsersResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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
    @Multipart
    suspend fun addUser(
        @Header("Token")
        token: String,
        @Part("name")
        name: RequestBody,
        @Part("email")
        email: RequestBody,
        @Part("phone")
        phone: RequestBody,
        @Part("position_id")
        positionId: RequestBody,
        @Part
        photo: MultipartBody.Part
    ): Response<UserResponse>

    @GET("token")
    suspend fun getToken(): Response<TokenResponse>

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