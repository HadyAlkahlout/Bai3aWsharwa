package com.raiyansoft.bai3awshrwa.model.categories

import com.google.gson.annotations.SerializedName

data class Category(
    var active: Int = 2,
    @SerializedName("ads")
    val ads: Int,
    @SerializedName("filters")
    val filters: List<Filter>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("subCategory")
    val subCategory: Int,
    @SerializedName("title")
    val title: String
)