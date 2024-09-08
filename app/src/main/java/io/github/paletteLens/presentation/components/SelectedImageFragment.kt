package io.github.paletteLens.presentation.components

import android.graphics.ImageDecoder
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.decodeBitmap
import coil.compose.rememberImagePainter
import io.github.paletteLens.domain.model.color.EnumSupportedPalettes
import io.github.paletteLens.domain.model.color.PaletteMetadata

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedImageFragment(
    modifier: Modifier = Modifier,
    viewModel: SelectedImageViewModel,
) {
    val context = LocalContext.current

    val selectedImage by viewModel.imageUriState.collectAsState()
    val isProcessing by viewModel.isProcessingState.collectAsState()
    val error by viewModel.error.collectAsState()

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
            Button(
                enabled = !isProcessing,
                onClick = {
                    val bitmap =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            val source = ImageDecoder.createSource(context.contentResolver, selectedImage!!)
                            ImageDecoder.decodeBitmap(source)
                        } else {
                            TODO("VERSION.SDK_INT < P")
                        }
                    val metadata = PaletteMetadata(kind = EnumSupportedPalettes.MATERIAL, numberOfColors = 5)

                    viewModel.extractPalette(metadata, bitmap)
                },
            ) {
                val text = if (isProcessing) "Extraindo paleta" else "Extrair paleta de cores"
                Text(text)
            }
        }

        when {
            error == true -> {
                BasicAlertDialog(
                    content = {
                        Card(
                            modifier =
                                modifier
                                    .requiredSize(300.dp, 70.dp),
                        ) {
                            Column(
                                modifier = modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Error,
                                    contentDescription = "Error icon",
                                )
                                Text("Erro ao extrair a paleta de cores")
                            }
                        }
                    },
                    onDismissRequest = { viewModel.resetError() },
                )
            }
        }
    }
}
