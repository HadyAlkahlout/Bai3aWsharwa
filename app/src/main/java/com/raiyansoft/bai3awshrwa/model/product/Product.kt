package com.raiyansoft.bai3awshrwa.model.product

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("category_id")
    val category_id: String,
    @SerializedName("category_title")
    val category_title: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("city_id")
    val city_id: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("fav")
    val fav: Boolean,
    @SerializedName("filters")
    val filters: List<Filter>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("note")
    val note: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("region_id")
    val region_id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("user_id")
    val user_id: Int,
    @SerializedName("user_image")
    val user_image: String,
    @SerializedName("user_name")
    val user_name: String,
    @SerializedName("whats")
    val whats: String,
    @SerializedName("special_status")
    val special_status: Boolean
)