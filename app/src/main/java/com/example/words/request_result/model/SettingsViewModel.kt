package com.example.words.request_result.model


import android.content.Context
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
            state.show.forEach{
                scope.launch {
                    saveSettings(it.key,true, context)
                }
            }
        } else{
            saved.forEach {
                state.show[it.key.name] = it.value as Boolean
            }
        }
    }

    suspend fun saveSettings(name: String, value: Boolean, context: Context) {
        val key = booleanPreferencesKey(name)
        state.show[name] = value

        context.dataStoreSettings.edit { show ->
            show[key] = value
        }
    }

    private suspend fun settingsIsSaved(context: Context): Map<Preferences.Key<*>, Any> {
        val preferences = context.dataStoreSettings.data.first()

        return preferences.asMap()
    }
}