package io.github.paletteLens.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.paletteLens.domain.model.UserState
import io.github.paletteLens.presentation.components.CameraFragment
import io.github.paletteLens.presentation.components.GalleryFragment
import io.github.paletteLens.presentation.components.SelectedImageFragment
import io.github.paletteLens.presentation.components.SelectedImageViewModel

@Composable
fun ExtractColorScreen(
    connectivityStatus: Boolean?,
    currentUser: UserState,
    loginRedirect: () -> Unit,
    modifier: Modifier = Modifier,
    sharedImageViewModel: SelectedImageViewModel,
    goToResult: () -> Unit,
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (connectivityStatus) {
            null -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
            }
            false -> {
                Text(
                    text = "Sem conexão com a internet",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            true -> {
                when (currentUser) {
                    is UserState.None -> {
                        loginRedirect()
                    }

                    is UserState.Loading -> {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                    }

                    is UserState.Loaded -> {
                        Text(
                            text = "Extrair paleta de cores de uma imagem",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        Spacer(modifier = modifier.height(16.dp))
                        // all image related fragments use the same ViewModel
                        CameraFragment(
                            viewModel = sharedImageViewModel,
                        )
                        GalleryFragment(
                            viewModel = sharedImageViewModel,
                        )
                        SelectedImageFragment(
                            viewModel = sharedImageViewModel,
                            goToResult = goToResult,
                        )
                    }
                }
            }
        }
    }
}
