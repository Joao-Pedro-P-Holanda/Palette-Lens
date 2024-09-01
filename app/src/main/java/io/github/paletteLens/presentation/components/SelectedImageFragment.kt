package io.github.paletteLens.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
) {
    val selectedImage by viewModel.imageUriState.collectAsState()

    if (selectedImage?.path?.isNotEmpty() == true) {
        Column {
            Image(
                modifier =
                    modifier
                        .padding(16.dp, 8.dp),
                painter = rememberImagePainter(selectedImage),
                contentDescription = null,
            )

            PaletteOptionDropdown(onItemClick = {
                // TODO add logic to handle palette option on viewmodel
                Log.e("SelectedImageFragment", "Palette Option: $it")
            })
            Button(onClick = {
                // TODO add logic to call a repository on viewmodel
            }) {
                Text("Extrair Paleta de cores")
            }
        }
    }
}
