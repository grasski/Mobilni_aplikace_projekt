package com.example.words.request_result.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.words.request_result.data.RequestBody
import com.example.words.request_result.data.EnumRequestErrors


data class RequestResultState(
    var requestBody: MutableState<RequestBody?> = mutableStateOf(null),
    var loading: MutableState<Boolean> = mutableStateOf(true),
    var error: MutableState<EnumRequestErrors> = mutableStateOf(EnumRequestErrors.NONE),
)
