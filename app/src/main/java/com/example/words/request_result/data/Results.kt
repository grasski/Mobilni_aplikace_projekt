package com.example.words.request_result.data

import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("definition") val definition : String,
    @SerializedName("partOfSpeech") val partOfSpeech : String,
    @SerializedName("synonyms") val synonyms : List<String>,
    @SerializedName("antonyms") val antonyms: List<String>,
    @SerializedName("typeOf") val typeOf : List<String>,
    @SerializedName("similarTo") val similarTo: List<String>,
    @SerializedName("examples") val examples: List<String>,
    @SerializedName("derivation") val derivation : List<String>
)
