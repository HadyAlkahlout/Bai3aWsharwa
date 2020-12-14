package com.raiyansoft.bai3awshrwa.model.notification

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("count_total")
    val count_total: Int,
    @SerializedName("data")
    val `data`: List<Notify>,
    @SerializedName("nextPageUrl")
    val nextPageUrl: String,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("un_read")
    val un_read: Int
)