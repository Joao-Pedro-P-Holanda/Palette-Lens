package io.github.paletteLens.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.rememberImagePainter
import io.github.paletteLens.presentation.components.ColorBox
import io.github.paletteLens.presentation.components.SelectedImageViewModel

@Composable
fun ExtractedResultScreen(
    modifier: Modifier = Modifier,
    sharedImageViewModel: SelectedImageViewModel,
) {
    val image by sharedImageViewModel.imageUriState.collectAsState()
    val currentPalette by sharedImageViewModel.currentPaletteState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(painter = rememberImagePainter(image), contentDescription = "imagem selecionada")
        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            currentPalette?.colors?.forEach { color ->
                ColorBox(color = color)
            }
        }
        Button(onClick = { }) {
            Text(text = "Salvar Paleta")
        }
    }
}
