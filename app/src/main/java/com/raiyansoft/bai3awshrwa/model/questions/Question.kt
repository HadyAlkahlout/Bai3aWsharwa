package com.raiyansoft.bai3awshrwa.model.questions

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("question")
    val question: String
){
    var open: Int = 0
}