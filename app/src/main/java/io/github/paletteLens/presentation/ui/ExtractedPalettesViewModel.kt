package io.github.paletteLens.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.paletteLens.domain.model.color.Palette
import io.github.paletteLens.repository.palette.PaletteRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExtractedPalettesViewModel
    @Inject
    constructor(private val paletteRepository: PaletteRepository) : ViewModel() {
        private val _extractedPalettes = MutableStateFlow<List<Palette>>(emptyList())

        private val _loadingState = MutableStateFlow(false)
        val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

        private val _errorState = MutableStateFlow<String?>(null)
        val errorState: StateFlow<String?> = _errorState.asStateFlow()

        private val _searchText = MutableStateFlow("")
        val searchText: StateFlow<String> = _searchText.asStateFlow()

        @OptIn(FlowPreview::class)
        val extractedPalettes =
            searchText.debounce(700L).combine(_extractedPalettes) { text, palettes ->
                if (text.isBlank()) {
                    palettes
                } else {
                    palettes.filter { palette ->
                        palette.metadata.name?.contains(text, ignoreCase = true) == true
                    }
                }
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                _extractedPalettes.value,
            )

        init {
            viewModelScope.launch {
                _loadingState.value = true
                try {
                    _extractedPalettes.value = paletteRepository.listPalettes()
                } catch (e: Exception) {
                    _errorState.value = e.message
                } finally {
                    _loadingState.value = false
                }
            }
        }

        fun refreshPalettes() {
            viewModelScope.launch {
                _loadingState.value = true
                try {
                    _extractedPalettes.value = paletteRepository.listPalettes()
                } catch (e: Exception) {
                    _errorState.value = e.message
                } finally {
                    _loadingState.value = false
                }
            }
        }

        fun onSearchTextChange(text: String) {
            _searchText.value = text
        }
    }
