package com.raiyansoft.bai3awshrwa.model.questions

import com.google.gson.annotations.SerializedName

data class Ques(
    @SerializedName("data")
    val `data`: List<Question>
)