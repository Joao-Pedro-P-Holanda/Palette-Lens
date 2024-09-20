package io.github.paletteLens.presentation.components

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.paletteLens.domain.model.color.Palette
import io.github.paletteLens.domain.model.color.PaletteMetadata
import io.github.paletteLens.repository.extractor.PaletteExtractorRepository
import io.github.paletteLens.repository.palette.PaletteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Unified ViewModel for the selected image, every Fragment that selects an image share this same ViewModel.
 */
@HiltViewModel
class SelectedImageViewModel
    @Inject
    constructor(private val repository: PaletteExtractorRepository, private val paletteRepository: PaletteRepository) : ViewModel() {
        private val _currentPaletteState = MutableStateFlow<Palette?>(null)
        val currentPaletteState = _currentPaletteState.asStateFlow()

        private val _imageUriState = MutableStateFlow(Uri.EMPTY)
        val imageUriState = _imageUriState.asStateFlow()

        private val _isProcessingState = MutableStateFlow(false)
        val isProcessingState = _isProcessingState.asStateFlow()

        private val _error = MutableStateFlow<Boolean?>(false)
        val error = _error.asStateFlow()

        private val paletteName = MutableStateFlow("")
        val paletteNameState = paletteName.asStateFlow()

        private val _showPaletteSaveDialog = MutableStateFlow(false)
        val showPaletteSaveDialog = _showPaletteSaveDialog.asStateFlow()

        fun setImageUri(uri: Uri) {
            _imageUriState.value = uri
            Log.e("SelectedImageViewModel", "Image URI: $uri")
        }

        fun resetError() {
            _error.value = false
        }

        fun extractPalette(
            metadata: PaletteMetadata,
            bitmap: Bitmap,
        ) {
            _isProcessingState.value = true
            viewModelScope.launch {
                try {
                    _currentPaletteState.value = repository.extractPalette(metadata, bitmap)
                } catch (e: Exception) {
                    _error.value = true
                    Log.e("SelectedImageViewModel", "Error extracting palette", e)
                } finally {
                    _isProcessingState.value = false
                }
            }
        }

        fun savePalette(name: String) {
            viewModelScope.launch {
                try {
                    _currentPaletteState.value?.metadata?.name = name
                    paletteRepository.createPalette(currentPaletteState.value!!)
                } catch (e: Exception) {
                    Log.e("SelectedImageViewModel", "Error saving palette", e)
                    _error.value = true
                }
            }
        }

        fun resetPalette() {
            _currentPaletteState.value = null
        }

        fun showPaletteSaveDialog() {
            _showPaletteSaveDialog.value = true
        }

        fun hidePaletteSaveDialog() {
            _showPaletteSaveDialog.value = false
        }

        fun setPaletteName(name: String) {
            paletteName.value = name
        }
    }
