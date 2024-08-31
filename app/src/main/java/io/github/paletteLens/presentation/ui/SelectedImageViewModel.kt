package io.github.paletteLens.presentation.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Unified ViewModel for the selected image, every Fragment that selects an image share this same ViewModel.
 */
class SelectedImageViewModel : ViewModel() {
    private val _imageUriState = MutableStateFlow(Uri.EMPTY)
    val imageUriState = _imageUriState.asStateFlow()

    fun setImageUri(uri: Uri) {
        _imageUriState.value = uri
        Log.e("SelectedImageViewModel", "Image URI: $uri")
    }
}
