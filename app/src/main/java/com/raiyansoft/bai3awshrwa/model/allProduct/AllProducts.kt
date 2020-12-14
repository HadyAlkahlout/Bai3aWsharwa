package com.raiyansoft.bai3awshrwa.model.allProduct

import com.google.gson.annotations.SerializedName
import com.raiyansoft.bai3awshrwa.model.general.Ad

data class AllProducts(
    @SerializedName("count_total")
    val count_total: Int,
    @SerializedName("data")
    val `data`: List<Ad>,
    @SerializedName("nextPageUrl")
    val nextPageUrl: String,
    @SerializedName("pages")
    val pages: Int
)