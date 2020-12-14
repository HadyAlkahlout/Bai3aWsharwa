package com.raiyansoft.bai3awshrwa.model.editProfile

import com.google.gson.annotations.SerializedName

data class EditProfile(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("city_id")
    val city_id: Int,
    @SerializedName("country_code")
    val country_code: Any,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("region_id")
    val region_id: Int,
    @SerializedName("user_id")
    val user_id: Int
)