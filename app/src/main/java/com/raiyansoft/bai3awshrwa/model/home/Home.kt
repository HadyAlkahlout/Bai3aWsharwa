package com.raiyansoft.bai3awshrwa.model.home

import com.google.gson.annotations.SerializedName
import com.raiyansoft.bai3awshrwa.model.general.Ad

data class Home(
    @SerializedName("down_banner")
    val down_banner: List<Banner>,
    @SerializedName("middle_banner")
    val middle_banner: List<Banner>,
    @SerializedName("new_ads")
    val new_ads: List<Ad>,
    @SerializedName("new_users")
    val new_users: List<User>,
    @SerializedName("special_ads")
    val special_ads: List<Ad>,
    @SerializedName("special_users")
    val special_users: List<User>,
    @SerializedName("up_banner")
    val up_banner: List<Banner>
)