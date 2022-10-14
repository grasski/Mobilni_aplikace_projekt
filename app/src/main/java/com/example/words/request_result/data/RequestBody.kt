package com.example.words.request_result.data

import com.google.gson.annotations.SerializedName

data class RequestBody(
    @SerializedName("results") val results : List<Results>,
    @SerializedName("syllables") val syllables : Syllables,
    @SerializedName("pronunciation") val pronunciation : Pronunciation
)
