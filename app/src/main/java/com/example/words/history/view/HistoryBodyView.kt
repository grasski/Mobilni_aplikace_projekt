package com.example.words.history.view


import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.words.animations.ContentAnimation
import com.example.words.history.model.HistoryViewModel
import com.example.words.request_result.model.SettingsViewModel
import com.example.words.request_result.view.Settings
import com.example.words.request_result.view.ShowResultBody
import com.example.words.request_result.view.ShowWordHeader
import com.example.words.shared.TopBar
import com.example.words.shared.TopBarActions


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryBodyView(
    navController: NavController,
    wordName: String,
    viewModel: HistoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    settingsViewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val wordObject = viewModel.state.wordObject.value

    val focusManager = LocalFocusManager.current
    var openSetting by remember { mutableStateOf(false) }

    // Get mobile screen width for animation
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val pxValue = LocalDensity.current.run { screenWidth.toPx() }
    var playAnimation by remember { mutableStateOf(false) }


    // Settings initialization
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit){
        settingsViewModel.init(scope, context)
    }

    // Initialization of the requested word body from history.
    LaunchedEffect(key1 = Unit){
        viewModel.loadResultBody(context, wordName)
    }

    Box{
        TopBar(
            title = wordName, navController = navController, TopBarActions.MENU,
            openSetting = {
                openSetting = it
                focusManager.clearFocus()
                playAnimation = true
            },
        ) {
            val listState = rememberLazyListState()
            val displayButton by remember { derivedStateOf { listState.firstVisibleItemIndex > 0} }
            var clickedShowHeader by remember { mutableStateOf(false) }
            val showHeader by rememberSaveable(clickedShowHeader, displayButton) {
                if(!displayButton){
                    clickedShowHeader = false
                    mutableStateOf(true)
                } else if (!clickedShowHeader){
                    mutableStateOf(true)
                } else {
                    mutableStateOf(false)
                }
            }

            LazyColumn(state = listState){
                // Info header of searched word
                stickyHeader {
                    Crossfade(
                        targetState = showHeader
                    ) { show ->
                        if (show) {
                            ShowWordHeader(wordObject, displayButton){
                                clickedShowHeader = it
                            }
                        }
                    }
                }

                item {
                    ShowResultBody(body = wordObject, settingsViewModel.state.show)
                }
            }
        }

        if (playAnimation){
            ContentAnimation().FadeInFromHorizontallySide(pxValue.toInt(), 400, openSetting, playAnimation = playAnimation){
                Settings({ openSetting = !it })
            }
        }
    }
}
