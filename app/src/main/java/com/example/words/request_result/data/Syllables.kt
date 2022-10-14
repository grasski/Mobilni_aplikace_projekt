package com.example.words.request_result.data

import com.google.gson.annotations.SerializedName

data class Syllables(
    @SerializedName("count") val count : Int,
    @SerializedName("list") val list : List<String>
)
