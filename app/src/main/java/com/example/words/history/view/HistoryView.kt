package com.example.words.history.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.words.R
import com.example.words.history.model.HistoryViewModel
import com.example.words.navigation.Routes
import com.example.words.shared.TopBar
import com.example.words.shared.TopBarActions


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryView(
    navController: NavController,
    viewModel: HistoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current

    TopBar(title = stringResource(id = R.string.historyText), navController = navController, action = TopBarActions.BACK, {}) {
        val requests = viewModel.state.requests
        LaunchedEffect(key1 = Unit){
            viewModel.loadHistory(context)
        }

        LazyColumn{
            item {
                requests.forEach {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(12.dp),
                        onClick = {
                            navController.navigate(Routes.HistoryBody.route + "/${it.key}")
                            Log.d("", "LALAA: " + it.value.pronunciation)
                        }
                    ){
                        Row(Modifier.padding(8.dp)){
                            Box(
                                Modifier
                                    .fillMaxWidth(0.5f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = it.key.replaceFirstChar{ it.uppercase() },
                                    textAlign = TextAlign.Start,
                                    fontSize = 23.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(
                                Modifier
                                    .fillMaxSize()
                            ){
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.6f),
                                    contentAlignment = Alignment.BottomEnd
                                ){
                                    Text(
                                        text = stringResource(id = R.string.lastSearch),
                                        fontSize = 12.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.BottomEnd
                                ){
                                    Text(it.value.searchedDate ?: "")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}