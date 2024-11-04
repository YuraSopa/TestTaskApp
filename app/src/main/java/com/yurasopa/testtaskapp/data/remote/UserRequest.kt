package com.yurasopa.testtaskapp.data.remote

import com.google.gson.annotations.SerializedName
import java.io.File

data class UserRequest(
    val name: String,
    val email: String,
    val phone: String,
    @SerializedName("position_id")
    val positionId: Int,
    val photo: File
)
