package com.example.words.navigation


import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.words.history.HistoryView
import com.example.words.request_result.view.RequestResultView


@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Search.route){
        composable(route = Routes.Search.route){
            RequestResultView(navController = navController)
        }
        composable(route = Routes.History.route){
            HistoryView(navController = navController)
        }
    }
}
