package com.raiyansoft.bai3awshrwa.model.seller

import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("date")
    val date: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("views")
    val views: Int
)