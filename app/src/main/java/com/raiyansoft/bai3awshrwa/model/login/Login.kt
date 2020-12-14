package com.raiyansoft.bai3awshrwa.model.login

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("token")
    val token: String,
    @SerializedName("user_id")
    val user_id: Int
)