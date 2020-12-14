package com.raiyansoft.bai3awshrwa.model.home

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("city")
    val city: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("region")
    val region: String
)