package com.raiyansoft.bai3awshrwa.model.seller

import com.google.gson.annotations.SerializedName
import com.raiyansoft.bai3awshrwa.model.general.Ad

data class Seller(
    @SerializedName("ads")
    val ads: List<Ad>,
    @SerializedName("info")
    val info: Info
)