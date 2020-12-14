package com.raiyansoft.bai3awshrwa.model.product

import com.google.gson.annotations.SerializedName

data class Filter(
    @SerializedName("filter_id")
    val filter_id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("value_text")
    val value_text: String
)