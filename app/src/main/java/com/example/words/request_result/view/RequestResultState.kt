package com.example.words.request_result.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.words.request_result.data.RequestBody


data class RequestResultState(
    var requestBody: MutableState<RequestBody?> = mutableStateOf(null),
    var loading: MutableState<Boolean> = mutableStateOf(true),
    var error: MutableState<Exception?> = mutableStateOf(null)
)
