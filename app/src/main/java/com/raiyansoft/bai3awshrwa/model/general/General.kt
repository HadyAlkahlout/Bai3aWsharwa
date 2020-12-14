package com.raiyansoft.bai3awshrwa.model.general

import com.google.gson.annotations.SerializedName

data class General(
    @SerializedName("code")
    val code: Int,
    @SerializedName("status")
    val status: Boolean
)