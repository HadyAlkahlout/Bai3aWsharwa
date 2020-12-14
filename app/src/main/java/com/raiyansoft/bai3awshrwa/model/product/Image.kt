package com.raiyansoft.bai3awshrwa.model.product

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String
)