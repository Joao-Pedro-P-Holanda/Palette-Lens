package io.github.paletteLens.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun SelectedImageFragment(
    modifier: Modifier = Modifier,
    viewModel: SelectedImageViewModel,
)  {
    val selectedImage by viewModel.imageUriState.collectAsState()

    if (selectedImage?.path?.isNotEmpty() == true) {
        Image(
            modifier =
                Modifier
                    .padding(16.dp, 8.dp),
            painter = rememberImagePainter(selectedImage),
            contentDescription = null,
        )
    }
}
