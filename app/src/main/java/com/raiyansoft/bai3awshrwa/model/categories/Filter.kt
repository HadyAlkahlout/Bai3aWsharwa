package com.raiyansoft.bai3awshrwa.model.categories

import com.google.gson.annotations.SerializedName

data class Filter(
    @SerializedName("id")
    val id: Int,
    @SerializedName("options")
    val options: List<Option>,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String
)