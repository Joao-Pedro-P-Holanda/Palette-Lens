package io.github.paletteLens.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.paletteLens.presentation.components.CameraFragment
import io.github.paletteLens.presentation.components.GalleryFragment
import io.github.paletteLens.presentation.components.SelectedImageFragment
import io.github.paletteLens.presentation.components.SelectedImageViewModel

@Composable
fun ExtractColorScreen(
    modifier: Modifier = Modifier,
    sharedImageViewModel: SelectedImageViewModel,
) {
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
        )
    }
}
