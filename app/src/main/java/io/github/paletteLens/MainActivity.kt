package io.github.paletteLens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import io.github.paletteLens.presentation.components.CameraFragment
import io.github.paletteLens.presentation.components.GalleryFragment
import io.github.paletteLens.presentation.components.SelectedImageFragment
import io.github.paletteLens.presentation.components.SelectedImageViewModel
import io.github.paletteLens.presentation.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sharedImageViewModel: SelectedImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasPermissions()) {
            requestPermissions(PERMISSIONS, 0)
        }
        enableEdgeToEdge()

        setContent {
            AppTheme {
                Column(
                    Modifier
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
        }
    }

    private fun hasPermissions(): Boolean {
        return PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val PERMISSIONS =
            arrayOf(
                android.Manifest.permission.CAMERA,
            )
    }
}
