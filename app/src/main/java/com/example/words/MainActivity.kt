package com.example.words

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.words.navigation.Navigation
import com.example.words.request_result.view.RequestResultView
import com.example.words.request_result.view.SettingsViewModel
import com.example.words.shared.TopBar
import com.example.words.ui.theme.WordsTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}
