package com.example.words.request_result.view

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.words.R
import com.example.words.animations.ContentAnimation
import com.example.words.shared.TopBar
import com.example.words.shared.TopBarActions


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RequestResultView(
    navController: NavController,
    viewModel: RequestResultViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var word by remember { mutableStateOf("") }
    var lastWord by remember { mutableStateOf(word) }

    val searchIcon = remember { Icons.Filled.Search }
    var searchClicked by remember { mutableStateOf(false) }
    var firstSearch by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var openSetting by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val pxValue = LocalDensity.current.run { screenWidth.toPx() }
    var pageStarted by remember { mutableStateOf(false) }

    Box {
        ContentAnimation().FadeInFromHorizontallySide(-pxValue.toInt(), 500, !openSetting, playAnimation = pageStarted){
            TopBar(title = stringResource(id = R.string.searchWordText), navController = navController, TopBarActions.MENU, openSetting = {
                openSetting = it
                pageStarted = true
            }) {
                Box(Modifier.fillMaxSize()){
                    LazyColumn(modifier = Modifier.fillMaxWidth()){
                        stickyHeader {
                            Box(
                                Modifier
                                    .background(MaterialTheme.colorScheme.background)
                                    .padding(top = 12.dp, bottom = 12.dp)
                            ){
                                TextField(
                                    value = word,
                                    onValueChange = { word = it },
                                    singleLine = true,
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            searchClicked = !searchClicked
                                            focusManager.clearFocus()
                                        }) {
                                            Icon(
                                                searchIcon,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    placeholder = { Text(stringResource(id = R.string.searchPlaceHolder)) },
                                    colors = TextFieldDefaults.textFieldColors(
                                        disabledTextColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent
                                    ),
                                    keyboardActions = KeyboardActions(onDone = {
                                        searchClicked = !searchClicked
                                        focusManager.clearFocus()
                                    }),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(20.dp))
                                )
                            }
                        }

                        item{
                            val body = viewModel.state.requestBody.value
                            val loading = viewModel.state.loading.value

                            LaunchedEffect(key1 = searchClicked){
                                if (word.isNotEmpty()){
                                    if (lastWord != word){
                                        Log.d("", "TED SE TO VOLA")
                                        firstSearch = true
                                        viewModel.callRequest(word)
                                    }
                                    lastWord = word
                                } else{
                                    Log.d("", "NEVOLAM")
                                }
                            }

                            if (firstSearch){
                                if (loading){
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 250.dp),
                                        contentAlignment = Alignment.TopCenter
                                    ){
                                        CircularProgressIndicator(
                                            Modifier
                                                .fillMaxSize(0.4f)
                                        )
                                    }
                                }else {
                                    Column(){
                                        body?.results?.forEach { it ->
                                            Text(text = "$it")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (pageStarted){
            ContentAnimation().FadeInFromHorizontallySide(pxValue.toInt(), 400, openSetting, playAnimation = pageStarted){
                Settings(){
                    openSetting = !it
                }
            }
        }
    }
}


@Composable
fun Settings(closeSettings: (Boolean) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Box(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.TopCenter
        ){
            Text(
                "Settings",
                Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = 25.sp
            )
            IconButton(onClick = { closeSettings(true) }, Modifier.align(Alignment.TopEnd)) {
                Icon(
                    Icons.Filled.Close,
                    null,
                    Modifier
                        .fillMaxSize(0.7f)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
