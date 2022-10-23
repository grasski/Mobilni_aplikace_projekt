package com.example.words.request_result.data

import com.google.gson.annotations.SerializedName

data class Pronunciation(
    @SerializedName("all") val all: String? = null,
    @SerializedName("noun") val noun: String? = null,
    @SerializedName("verb") val verb: String? = null
)
