package com.example.words.history

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.words.R
import com.example.words.shared.TopBar
import com.example.words.shared.TopBarActions


@Composable
fun HistoryView(navController: NavController) {
    TopBar(title = stringResource(id = R.string.historyText), navController = navController, action = TopBarActions.BACK, {}) {
        Text(text = "lala")
    }
}