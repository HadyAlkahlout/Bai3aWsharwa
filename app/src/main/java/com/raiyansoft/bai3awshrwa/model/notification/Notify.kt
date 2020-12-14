package com.raiyansoft.bai3awshrwa.model.notification

import com.google.gson.annotations.SerializedName

data class Notify(
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("read")
    val read: Int,
    @SerializedName("title")
    val title: String
)