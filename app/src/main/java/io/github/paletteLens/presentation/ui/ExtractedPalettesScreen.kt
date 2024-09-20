package io.github.paletteLens.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.paletteLens.presentation.components.PaletteCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ExtractedPalettesScreen(
    modifier: Modifier = Modifier,
    extractedPalettesViewModel: ExtractedPalettesViewModel,
) {
    val extractedPalettes by extractedPalettesViewModel.extractedPalettes.collectAsState()
    val loadingState by extractedPalettesViewModel.loadingState.collectAsState()
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    val pullRefreshState =
        rememberPullRefreshState(
            onRefresh = {
                extractedPalettesViewModel.refreshPalettes()
            },
            refreshing = isRefreshing,
        )

    Column(
        modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (loadingState) {
            true ->
                CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)

            false -> {
                Text(
                    text = "Lista de paletas",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                when (extractedPalettes.isEmpty()) {
                    true ->
                        Text(
                            text = "Nenhuma paleta adicionada ainda",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )

                    false ->
                        LazyColumn(
                            modifier
                                .padding(12.dp)
                                .pullRefresh(pullRefreshState),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(extractedPalettes) {
                                PaletteCard(palette = it)
                            }
                        }
                }
            }
        }
    }
}
