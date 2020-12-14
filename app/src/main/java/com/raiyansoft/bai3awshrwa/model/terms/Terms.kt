package com.raiyansoft.bai3awshrwa.model.terms

import com.google.gson.annotations.SerializedName

data class Terms(
    @SerializedName("conditions")
    val conditions: String
)