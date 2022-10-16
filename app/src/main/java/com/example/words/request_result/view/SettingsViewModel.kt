package com.example.words.request_result.view


import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStoreSettings by preferencesDataStore("settings")


class SettingsViewModel: ViewModel() {
    val state by mutableStateOf(SettingsState())


    suspend fun init(scope: CoroutineScope, context: Context) {
        var saved = mapOf<Preferences.Key<*>, Any>()

        scope.launch {
            saved = settingsIsSaved(context)
        }.join()

        if (saved.isEmpty()){
            state.show.forEachIndexed { i, v ->
                scope.launch {
                    saveSettings(i, true, context)
                }
            }
        } else{
            saved.forEach{
                state.show[it.key.name.toInt()] = it.value as Boolean
            }
        }
    }

    suspend fun saveSettings(id: Int, value: Boolean, context: Context) {
        val key = booleanPreferencesKey(id.toString())
        state.show[id] = value

        context.dataStoreSettings.edit { show ->
            show[key] = value
        }
    }

    private suspend fun settingsIsSaved(context: Context): Map<Preferences.Key<*>, Any> {
        val preferences = context.dataStoreSettings.data.first()

        return preferences.asMap()
    }
}