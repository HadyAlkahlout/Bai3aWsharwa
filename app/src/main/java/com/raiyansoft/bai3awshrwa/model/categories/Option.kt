package com.raiyansoft.bai3awshrwa.model.categories

import com.google.gson.annotations.SerializedName

data class Option(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
)