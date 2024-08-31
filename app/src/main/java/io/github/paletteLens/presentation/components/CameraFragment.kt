package io.github.paletteLens.presentation.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import io.github.paletteLens.BuildConfig
import java.io.File
import java.util.Date
import java.util.Objects

@Composable
fun CameraFragment(
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
    val file = context.createImageFile()
    val uri =
        FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            BuildConfig.APPLICATION_ID + ".provider",
            file,
        )
    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                viewModel.setImageUri(uri)
            } else {
                Log.e("CameraFragment", "Failed to take picture")
            }
            Log.e("CameraFragment", "Image URI: $imageURI")
            Log.e("CameraFragment", "URI: $uri")
        }

    fun takePicture() {
        try {
            takePictureLauncher.launch(uri)
        } catch (e: ActivityNotFoundException) {
            Log.e("CameraFragment", "Activity not found", e)
        }
    }

    Button(onClick = { takePicture() }) {
        Text("Usar foto da câmera")
    }
}
