package com.example.words.history.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    var toDelete by remember { mutableStateOf("") }
    var openDialog by remember { mutableStateOf(false) }

    TopBar(title = stringResource(id = R.string.historyText), navController = navController, action = TopBarActions.BACK, {}) {
        val requests = viewModel.state.requests
        LaunchedEffect(key1 = Unit){
            viewModel.loadHistory(context)
        }

        var delete by remember { mutableStateOf(false) }
        if (openDialog){
            DeleteDialog(toDelete, { openDialog = it }, { delete = it })
        }
        if (delete){
            LaunchedEffect(key1 = Unit){
                viewModel.deleteFromHistory(context, toDelete)
                delete = false
            }
        }

        LazyColumn{
            item{
                requests.forEach {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(12.dp),
                        onClick = {
                            navController.navigate(Routes.HistoryBody.route + "/${it.key}")
                        }
                    ){
                        Box(Modifier.padding(8.dp)){
                            Box(
                                Modifier
                                    .fillMaxWidth(0.6f)
                                    .fillMaxHeight()
                                    .padding(start = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                Text(
                                    text = it.key.replaceFirstChar{ it.uppercase() },
                                    textAlign = TextAlign.Start,
                                    fontSize = 23.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.BottomEnd
                            ){
                                Row(
                                    verticalAlignment = Alignment.Bottom
                                ){
                                    Text(
                                        text = stringResource(id = R.string.lastSearch),
                                        fontSize = 12.sp,
                                    )

                                    Text(it.value.searchedDate ?: "", fontSize = 15.sp)
                                }
                            }

                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                                contentAlignment = Alignment.TopEnd
                            ){
                                IconButton(
                                    onClick = {
                                        openDialog = true
                                        toDelete = it.key
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        "",
                                        Modifier.size(30.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DeleteDialog(wordName: String, openDialogRet: (Boolean) -> Unit, delete: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = {
            openDialogRet(false)
            delete(false)
        },
        title = {
            Text(text = "Odstranit")
        },
        text = {
            Text(text = "Opravdu si přejete odstranit $wordName z historie?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openDialogRet(false)
                    delete(true)
                }
            ) {
                Text("ANO")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialogRet(false)
                    delete(false)
                }
            ) {
                Text("NE")
            }
        }
    )
}
