package com.example.words.history.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.words.request_result.data.RequestBody


data class HistoryState (
    var requests: SnapshotStateMap<String, RequestBody> = mutableStateMapOf(),
    var wordObject: MutableState<RequestBody?> = mutableStateOf(null)
)