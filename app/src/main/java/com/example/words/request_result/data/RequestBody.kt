package com.example.words.request_result.data


import com.google.gson.annotations.SerializedName

data class RequestBody(
    @SerializedName("word") val word : String? = null,
    @SerializedName("results") val results : List<Results>? = null,
    @SerializedName("syllables") val syllables : Syllables? = null,
    @SerializedName("pronunciation") val pronunciation : Pronunciation? = null,
//    @SerializedName("pronunciation") val pronunciationS : String? = null,
    var searchedDate: String? = null
)