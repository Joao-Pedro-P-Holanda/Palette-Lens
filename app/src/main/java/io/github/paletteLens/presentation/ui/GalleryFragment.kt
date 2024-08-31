package io.github.paletteLens.presentation.ui

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun GalleryFragment(
    modifier: Modifier = Modifier,
    viewModel: SelectedImageViewModel,
) {
    val context = LocalContext.current
    val imageURI by viewModel.imageUriState.collectAsState()

    fun Context.createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val image =
            File.createTempFile(
                imageFileName, // prefix
                ".jpg", // suffix
                externalCacheDir, // directory
            )
        return image
    }

    val selectPictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                viewModel.setImageUri(uri)
            } else {
                Log.e("GalleryFragment", "Failed to get image from gallery")
            }
        }

    fun selectPicture() {
        selectPictureLauncher.launch("image/*")
    }

    Button(onClick = { selectPicture() }) {
        Text("Selecionar imagem da galeria")
    }
}
