package io.github.paletteLens.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import io.github.paletteLens.presentation.components.ColorBox
import io.github.paletteLens.presentation.components.SelectedImageViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExtractedResultScreen(
    modifier: Modifier = Modifier,
    sharedImageViewModel: SelectedImageViewModel,
) {
    val image by sharedImageViewModel.imageUriState.collectAsState()
    val currentPalette by sharedImageViewModel.currentPaletteState.collectAsState()
    val showPaletteSaveDialog by sharedImageViewModel.showPaletteSaveDialog.collectAsState()
    val paletteNameState by sharedImageViewModel.paletteNameState.collectAsState()
    // rendering each color box in a launched effect to not block main thread
    var colorBoxes = remember { listOf<@Composable () -> Unit>() }

    LaunchedEffect(currentPalette) {
        colorBoxes = currentPalette?.colors?.map {
                color ->
            @Composable {
                ColorBox(
                    color = color,
                    detailed = true,
                )
            }
        } ?: emptyList()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { padding ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (colorBoxes.isEmpty()) {
                true -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                }

                false -> {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = "Resultado",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = modifier.height(16.dp))
                    Image(
                        painter = rememberImagePainter(image),
                        contentDescription = "imagem selecionada",
                    )
                    Spacer(modifier = modifier.height(8.dp))
                    Text(
                        text = "Cores extraídas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    FlowRow(
                        modifier.fillMaxWidth().padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        currentPalette?.colors?.forEach { color ->
                            ColorBox(
                                modifier = modifier.size(70.dp),
                                color = color,
                                detailed = true,
                            )
                        }
                    }
                    Spacer(modifier = modifier.height(16.dp))
                    Button(onClick = {
                        sharedImageViewModel.showPaletteSaveDialog()
                    }) {
                        Text(text = "Salvar paleta")
                    }

                    when (showPaletteSaveDialog) {
                        true -> {
                            BasicAlertDialog(
                                content = {
                                    Card {
                                        TextField(
                                            value = paletteNameState,
                                            onValueChange = { sharedImageViewModel.setPaletteName(it) },
                                            label = { Text("Nome da paleta") },
                                        )
                                        Button(
                                            onClick = {
                                                scope.launch {
                                                    sharedImageViewModel.savePalette(paletteNameState)
                                                    sharedImageViewModel.hidePaletteSaveDialog()
                                                    snackbarHostState.showSnackbar("Paleta salva com sucesso!")
                                                }
                                            },
                                            enabled = paletteNameState.isNotBlank(),
                                        ) {
                                            Text(text = "Salvar")
                                        }
                                    }
                                },
                                onDismissRequest = {
                                    sharedImageViewModel.hidePaletteSaveDialog()
                                },
                            )
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }
}
