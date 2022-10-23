package com.example.words.request_result.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.words.R
import com.example.words.request_result.model.SettingsEnum
import com.example.words.request_result.model.SettingsViewModel
import kotlinx.coroutines.launch


@Composable
fun Settings(
    closeSettings: (Boolean) -> Unit,
    viewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val hover = MutableInteractionSource()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .hoverable(hover)
            .background(MaterialTheme.colorScheme.background)
    ){
        Box(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            contentAlignment = Alignment.TopCenter
        ){
            Text(
                stringResource(id = R.string.settingsText),
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

        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ){
            viewModel.state.show.forEach{ (key, value) ->
                var checked by remember { mutableStateOf(value) }
                val interactionSource = remember { MutableInteractionSource() }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                ){
                    Checkbox(checked = checked, onCheckedChange = {
                        checked = it
                        scope.launch {
                            viewModel.saveSettings(key, it, context)
                        }
                    })

                    Text(
                        text = SettingsEnum.valueOf(key).getInSettingsText(context),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                checked = !checked
                                scope.launch {
                                    viewModel.saveSettings(key, checked, context)
                                }
                            },
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
