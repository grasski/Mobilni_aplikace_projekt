package com.example.words.navigation


sealed class Routes(val route: String) {
    object Search : Routes("search")
    object History : Routes("history")
}