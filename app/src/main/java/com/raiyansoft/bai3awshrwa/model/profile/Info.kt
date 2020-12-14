package com.raiyansoft.bai3awshrwa.model.profile

import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("ads")
    val ads: Int,
    @SerializedName("broker")
    val broker: Boolean,
    @SerializedName("city")
    val city: String,
    @SerializedName("city_id")
    val city_id: Int,
    @SerializedName("country_code")
    val country_code: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("favorites")
    val favorites: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
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
    @SerializedName("send_notification")
    val send_notification: Boolean,
    @SerializedName("special_status")
    val special_status: Boolean,
    @SerializedName("views")
    val views: Int
)