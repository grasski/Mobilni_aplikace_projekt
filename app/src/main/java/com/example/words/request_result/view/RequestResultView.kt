package com.example.words.request_result.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.words.R
import com.example.words.animations.ContentAnimation
import com.example.words.history.model.HistoryViewModel
import com.example.words.request_result.data.EnumCardColors
import com.example.words.request_result.data.EnumRequestErrors
import com.example.words.request_result.data.RequestBody
import com.example.words.request_result.model.RequestResultViewModel
import com.example.words.request_result.model.SettingsEnum
import com.example.words.request_result.model.SettingsViewModel
import com.example.words.shared.TopBar
import com.example.words.shared.TopBarActions
import kotlinx.coroutines.launch
import kotlin.random.Random


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RequestResultView(
    navController: NavController,
    viewModel: RequestResultViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    settingsViewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    var word by remember { mutableStateOf("") }
    var lastWord by remember { mutableStateOf(word) }

    // Settings initialization
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit){
        settingsViewModel.init(scope, context)
    }

    val searchIcon = remember { Icons.Filled.Search }
    var searchClicked by remember { mutableStateOf(false) }
    var firstSearch by rememberSaveable { mutableStateOf(false) }   // For loading spinner animation to be played only after search event
    val focusManager = LocalFocusManager.current
    var openSetting by remember { mutableStateOf(false) }

    // Get mobile screen width for animation
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val pxValue = LocalDensity.current.run { screenWidth.toPx() }
    var playAnimation by remember { mutableStateOf(false) }

    Box {
        TopBar(
            title = stringResource(id = R.string.searchWordText),
            navController = navController,
            TopBarActions.MENU,
            openSetting = {
                openSetting = it

                focusManager.clearFocus()
                playAnimation = true
            }
        ) {
            Box(Modifier.fillMaxSize()) {
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
                val coroutineScope = rememberCoroutineScope()

                val body = viewModel.state.requestBody.value
                val loading = viewModel.state.loading.value

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = listState
                ) {

                    // Header with textfield for word search
                    stickyHeader {
                        Box(
                            Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .padding(top = 12.dp, bottom = 12.dp)
                        ) {
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

                    // Info header of searched word
                    stickyHeader {
                        if (!loading){
                            Crossfade(
                                targetState = showHeader
                            ) { show ->
                                if (show) {
                                    ShowWordHeader(body, displayButton){
                                        clickedShowHeader = it
                                    }
                                }
                            }
                        }
                    }

                    item {
                        val error = viewModel.state.error.value

                        // Once the 'body' change, the whole request is saved to history
                        LaunchedEffect(key1 = body){
                            if (body != null && word != "") {
                                HistoryViewModel().saveRequestToHistory(word, body, context)
                            }
                        }

                        // Effect for handling word searching
                        LaunchedEffect(key1 = searchClicked) {
                            if (word.isNotEmpty()) {
                                if (lastWord != word || error != EnumRequestErrors.NONE) {
                                    firstSearch = true
                                    viewModel.callRequest(word)
                                }
                                lastWord = word
                            }
                        }

                        // Condition used to don't run Loading animation at the start of application
                        if (firstSearch) {
                            if (loading) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 250.dp)
                                        .padding(12.dp),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    CircularProgressIndicator(
                                        Modifier
                                            .fillMaxSize(0.4f)
                                    )
                                }
                            }
                            else if (error != EnumRequestErrors.NONE){
                                ShowError(error, searchClicked){
                                    searchClicked = it
                                }
                            } else {
                                ShowResultBody(body, settingsViewModel.state.show)
                            }
                        }
                    }

                }

                // Button which will appear on after user scroll down in result body.
                // Used to jump to the top of result body.
                AnimatedVisibility(
                    visible = displayButton,
                    Modifier.align(Alignment.BottomEnd)
                ) {
                    OutlinedIconButton(
                        onClick = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(100),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .padding(25.dp)
                            .size(50.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowUpward,
                            "",
                            modifier = Modifier
                                .padding(2.dp)
                                .size(36.dp)
                        )
                    }
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


@Composable
fun ResultsDivider(index: Int) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp),
        contentAlignment = Alignment.Center
    ){
        Divider(thickness = 2.dp)
        Text(
            "Výsledek ${index+1}",
            textAlign = TextAlign.Center,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
fun ShowWordHeader(body: RequestBody?, displayButton: Boolean, clickedShowHeader: (Boolean) -> Unit) {
    body?.let {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box {
                        Text(
                            text = it.word?.uppercase() ?: "",
                            fontSize = 23.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(36.dp)
                        ){
                            Crossfade(
                                targetState = displayButton
                            ) {
                                if (it) {
                                    IconButton(
                                        onClick = { clickedShowHeader(true) },
                                    ) {
                                        Icon(
                                            Icons.Default.Visibility,
                                            ""
                                        )
                                    }
                                }
                            }
                        }

                    }
                    val syllTxt by remember {
                        mutableStateOf(
                            it.syllables?.count.toString() + " => " + it.syllables?.list?.joinToString(
                                "-"
                            )
                        )
                    }
                    Text(stringResource(R.string.syllables).uppercase() + syllTxt)

                    it.pronunciation?.let { pron ->
                        Row {
                            Text(
                                stringResource(R.string.pronunciation).uppercase()
                                    .uppercase(),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )

                            if (!pron.all.isNullOrEmpty()) {
                                Text(pron.all ?: "")
                            } else {
                                Column {
                                    Text(stringResource(R.string.noun) + pron.noun)
                                    Text(stringResource(R.string.verb) + pron.verb)
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
fun ShowError(error: EnumRequestErrors, clicked: Boolean, searchClicked: (Boolean) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .heightIn(min = 250.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(error){
            EnumRequestErrors.PERMISSION_DENIED -> {
                Text(stringResource(id = R.string.permissionDeniedMessage))
                Button(
                    onClick = {
                        searchClicked(!clicked)
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )

                ) {
                    Text(
                        stringResource(id = R.string.tryAgain),
                        fontSize = 18.sp
                    )
                }
            }
            EnumRequestErrors.WORD_DO_NOT_EXISTS -> {
                NothingToShow()
            }

            else -> {}
        }
    }
}

@Composable
fun NothingToShow() {
    Card(
        Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ){
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(text = stringResource(id = R.string.nothingToShow), fontSize = 30.sp)
        }
    }
}

@Composable
fun ShowResultBody(body: RequestBody?, settings: SnapshotStateMap<String, Boolean>) {
    val colorsCount by rememberSaveable { mutableStateOf(EnumCardColors.values().size) }
    val context = LocalContext.current
    val randomColorOffset = remember { (0 until colorsCount-1).random() }

    body?.results?.let { results ->
        results.forEachIndexed { i, result ->
            ResultsDivider(i)

            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(containerColor = EnumCardColors.values()[(i + randomColorOffset) % colorsCount].color())
            ){
                settings.forEach {
                    if (it.value){
                        Text(
                            SettingsEnum.valueOf(it.key).getResultTitle(context).uppercase() + SettingsEnum.valueOf(it.key).getResult(result),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
    
    if (body?.results == null){
        NothingToShow()
    }
}