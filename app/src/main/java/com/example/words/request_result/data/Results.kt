package com.example.words.request_result.data

import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("definition") val definition : String? = null,
    @SerializedName("partOfSpeech") val partOfSpeech : String? = null,
    @SerializedName("synonyms") val synonyms : List<String>? = null,
    @SerializedName("antonyms") val antonyms: List<String>? = null,
    @SerializedName("typeOf") val typeOf : List<String>? = null,
    @SerializedName("similarTo") val similarTo: List<String>? = null,
    @SerializedName("examples") val examples: List<String>? = null,
    @SerializedName("derivation") val derivation : List<String>? = null,

    @SerializedName("hasCategories") val categories : List<String>? = null,
    @SerializedName("hasTypes") val types : List<String>? = null,
)
