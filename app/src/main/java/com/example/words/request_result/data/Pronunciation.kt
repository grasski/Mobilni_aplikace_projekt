package com.example.words.request_result.data

import com.google.gson.annotations.SerializedName

data class Pronunciation(
    @SerializedName("all") val all : String,
    @SerializedName("noun") val noun : String,
    @SerializedName("verb") val verb : String
)
