package com.raiyansoft.bai3awshrwa.model.usagePolicy

import com.google.gson.annotations.SerializedName

data class Policy(
    @SerializedName("privacy")
    val privacy: String
)