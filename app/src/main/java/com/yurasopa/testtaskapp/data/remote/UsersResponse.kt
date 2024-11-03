package com.yurasopa.testtaskapp.data.remote


import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("links")
    val links: Links,
    @SerializedName("page")
    val page: Int,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_users")
    val totalUsers: Int,
    @SerializedName("users")
    val users: List<User>
)