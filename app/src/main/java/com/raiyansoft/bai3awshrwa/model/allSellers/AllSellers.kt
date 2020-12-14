package com.raiyansoft.bai3awshrwa.model.allSellers

import com.google.gson.annotations.SerializedName
import com.raiyansoft.bai3awshrwa.model.home.User

data class AllSellers(
    @SerializedName("count_total")
    val count_total: Int,
    @SerializedName("data")
    val `data`: List<User>,
    @SerializedName("nextPageUrl")
    val nextPageUrl: String,
    @SerializedName("pages")
    val pages: Int
)