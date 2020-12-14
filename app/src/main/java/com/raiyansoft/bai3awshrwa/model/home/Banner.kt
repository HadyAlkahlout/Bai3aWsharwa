package com.raiyansoft.bai3awshrwa.model.home

import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("image")
    val image: String,
    @SerializedName("url")
    val url: String
)