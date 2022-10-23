package com.example.words.history.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
        val key = stringPreferencesKey(word)
        val json: String = Gson().toJson(request)

        context.dataStoreHistory.edit {
            if(it[key].isNullOrEmpty()){
                it[key] = json
            }
        }
    }

    suspend fun loadHistory(context: Context): Map<String, RequestBody> {
        val preferences = context.dataStoreHistory.data.first()
        val preferencesMap = preferences.asMap()

        val history = mutableStateMapOf<String, RequestBody>()
        preferencesMap.forEach{ (key, value) ->
            val k = key.name

            if (!history.containsKey(k) && k != ""){
                val v = Gson().fromJson(value.toString(), RequestBody::class.java)
                history[k] = v
                state.requests[k] = v
            }
        }

        return history
    }

    suspend fun loadResultBody(context: Context, wordName: String) {
        val key = stringPreferencesKey(wordName)
        val preferences = context.dataStoreHistory.data.first()

        state.wordObject.value = Gson().fromJson(preferences[key], RequestBody::class.java)
    }

}