package com.example.words.request_result.view

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList


data class SettingsState(
    var show: SnapshotStateList<Boolean?> = mutableStateListOf(
        null,       // showCategory (typeOf)
        null,      // showDefinition
        null,      // showSynonyms
        null,       // showAntonyms
        null        // showExamples
    ),
)
