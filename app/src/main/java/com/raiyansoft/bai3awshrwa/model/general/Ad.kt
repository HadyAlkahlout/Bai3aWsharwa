package com.raiyansoft.bai3awshrwa.model.general

import com.google.gson.annotations.SerializedName

data class Ad(
    @SerializedName("category_id")
    val category_id: String,
    @SerializedName("category_title")
    val category_title: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("title")
    val title: String
)