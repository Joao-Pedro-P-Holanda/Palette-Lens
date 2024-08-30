package io.github.paletteLens.presentation.ui

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import io.github.paletteLens.BuildConfig
import java.io.File
import java.util.Date
import java.util.Objects

@Composable
fun CameraFragment(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var imageURI by remember {
        mutableStateOf<Uri?>(Uri.EMPTY)
    }

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
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { _ ->
            imageURI = uri
            Log.e("CameraFragment", "Image URI: $imageURI")
        }

    fun takePicture() {
        takePictureLauncher.launch(uri)
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { takePicture() }) {
            val text = if (imageURI?.path?.isNotEmpty() == true) "Alterar foto" else "Tirar foto"
            Text(text)
        }
        if (imageURI?.path?.isNotEmpty() == true) {
            Image(
                modifier =
                    modifier
                        .padding(16.dp, 8.dp),
                painter = rememberImagePainter(imageURI),
                contentDescription = null,
            )
        }
    }
}
