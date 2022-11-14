package com.example.words.history.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.example.words.request_result.data.RequestBody
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

private val Context.dataStoreHistory by preferencesDataStore("history")

class HistoryViewModel: ViewModel() {
    val state by mutableStateOf(HistoryState())

    suspend fun saveRequestToHistory(word: String, request: RequestBody, context: Context){
        val key = stringPreferencesKey(word.lowercase())
        val json: String = Gson().toJson(request)

        context.dataStoreHistory.edit {
            it[key] = json
        }
    }

    suspend fun loadHistory(context: Context) {
        val preferences = context.dataStoreHistory.data.first()
        val preferencesMap = preferences.asMap()

        preferencesMap.forEach{ (key, value) ->
            val k = key.name

            if (!state.requests.containsKey(k) && k != ""){
                val v = Gson().fromJson(value.toString(), RequestBody::class.java)
                state.requests[k] = v
            }
        }
    }

    suspend fun wordExistsInHistory(context: Context, wordName: String): Boolean{
        val preferences = context.dataStoreHistory.data.first()

        preferences.asMap().map {
            if (it.key.name == wordName.lowercase()){
                return true
            }
        }
        return false
    }

    suspend fun loadResultBody(context: Context, wordName: String) {
        val key = stringPreferencesKey(wordName.lowercase())
        val preferences = context.dataStoreHistory.data.first()

        state.wordObject.value = Gson().fromJson(preferences[key], RequestBody::class.java)
    }

    suspend fun deleteFromHistory(context: Context, wordName: String){
        val key = stringPreferencesKey(wordName.lowercase())
        context.dataStoreHistory.edit {
            it.remove(key)
        }
        state.requests.remove(wordName.lowercase())
    }

}