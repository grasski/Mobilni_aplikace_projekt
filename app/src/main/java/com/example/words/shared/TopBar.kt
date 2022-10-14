package com.example.words.shared


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.words.R
import com.example.words.navigation.Routes
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "MutableCollectionMutableState",
    "CoroutineCreationDuringComposition"
)
@Composable
fun TopBar(
    title: String,
    navController: NavController,
    action: TopBarActions,
    openSetting: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            } else{
                                drawerState.open()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null
                        )
                    }
                },
                actions ={
                    scope.launch {
                        if (drawerState.isOpen) {
                            drawerState.close()
                        }
                    }

                    when(action){
                        TopBarActions.BACK -> {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = null)
                            }
                        }
                        TopBarActions.MENU -> {
                            IconButton(onClick = {
                                openSetting(true)
                                // TODO: open settings to select request data
                            }) {
                                Icon(Icons.Filled.MoreVert, contentDescription = null)
                            }
                        }
                    }
                },
            )
        },
        content = { innerPadding ->
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    Box(modifier = Modifier.padding(innerPadding)){
                        DrawerView(navController = navController)
                    }
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        content()
                    }
                }
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerView(
    navController: NavController
) {
    val searchIcon = remember { Icons.Filled.Search }
    val historyIcon = remember { Icons.Default.History }

    val searchTitleText = stringResource(id = R.string.searchWordText)
    val historyTileText = stringResource(id = R.string.historyText)

    val scope = rememberCoroutineScope()

    ModalDrawerSheet() {
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { Icon(searchIcon, contentDescription = null) },
            label = { Text(searchTitleText, fontSize = 22.sp) },
            selected = navController.currentDestination?.route == Routes.Search.route,
            onClick = {
                scope.launch {
                    navController.navigate(Routes.Search.route)
                }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            icon = { Icon(historyIcon, contentDescription = null) },
            label = { Text(historyTileText, fontSize = 22.sp) },
            selected = navController.currentDestination?.route == Routes.History.route,
            onClick = {
                scope.launch {
                    navController.navigate(Routes.History.route)
                }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
