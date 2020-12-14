package com.raiyansoft.bai3awshrwa.model.settings

import com.google.gson.annotations.SerializedName

data class Setting(
    @SerializedName("android_version")
    val android_version: String,
    @SerializedName("force_close")
    val force_close: String,
    @SerializedName("force_update")
    val force_update: String,
    @SerializedName("ios_version")
    val ios_version: String,
    @SerializedName("special")
    val special: String
)