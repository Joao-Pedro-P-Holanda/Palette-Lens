package io.github.paletteLens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import io.github.paletteLens.presentation.theme.AppTheme
import io.github.paletteLens.presentation.ui.CameraFragment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasPermissions()) {
            requestPermissions(PERMISSIONS, 0)
        }
        enableEdgeToEdge()
        setContent {
            AppTheme {
                CameraFragment(
                    modifier = Modifier.fillMaxSize(),
                )
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
