package com.example.words.request_result.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.words.BuildConfig.API_KEY
import com.example.words.request_result.data.EnumRequestErrors
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class RequestResultViewModel: ViewModel() {
    val state by mutableStateOf(RequestResultState())
    private var client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson{
                setPrettyPrinting()
                disableHtmlEscaping()
            }
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
                state.error.value = EnumRequestErrors.NONE

                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                val formatted = current.format(formatter)
                state.requestBody.value?.searchedDate = formatted
            } else{
                state.error.value = EnumRequestErrors.WORD_DO_NOT_EXISTS
                state.requestBody.value = null
            }

            state.loading.value = false
        } catch (e: Exception){
            state.error.value = EnumRequestErrors.PERMISSION_DENIED

            state.loading.value = false
            state.requestBody.value = null
        }
    }

}