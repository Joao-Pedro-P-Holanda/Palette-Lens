package io.github.paletteLens.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.paletteLens.domain.model.UserState
import io.github.paletteLens.presentation.components.CameraFragment
import io.github.paletteLens.presentation.components.GalleryFragment
import io.github.paletteLens.presentation.components.SelectedImageFragment
import io.github.paletteLens.presentation.components.SelectedImageViewModel

@Composable
fun ExtractColorScreen(
    currentUser: UserState,
    loginRedirect: () -> Unit,
    modifier: Modifier = Modifier,
    sharedImageViewModel: SelectedImageViewModel,
    goToResult: () -> Unit,
) {
    when (currentUser) {
        is UserState.None -> {
            loginRedirect()
        }

        is UserState.Loading -> {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
        }

        is UserState.Loaded -> {
            Column(
                modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
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
