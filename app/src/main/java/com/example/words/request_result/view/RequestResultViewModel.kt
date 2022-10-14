package com.example.words.request_result.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.words.BuildConfig.API_KEY
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*


class RequestResultViewModel: ViewModel() {
    val state by mutableStateOf(RequestResultState())
    private var client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun callRequest(word: String){
        state.loading.value = true
        try {
            val response = client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    url("https://wordsapiv1.p.rapidapi.com/words/${word}")
                    headers {
                        append("X-RapidAPI-Key", API_KEY)
                        append("X-RapidAPI-Host", "wordsapiv1.p.rapidapi.com")
                    }
                }
            }

            if (response.status == HttpStatusCode.OK){
                state.requestBody.value = response.body()
            } else{
                state.requestBody.value = null
            }

            state.loading.value = false
            state.error.value = null
        } catch (e: Exception){
            state.error.value = e
            state.loading.value = false
            state.requestBody.value = null
        }
    }
}