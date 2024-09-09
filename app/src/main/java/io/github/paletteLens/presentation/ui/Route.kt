package io.github.paletteLens.presentation.ui

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * This class defines an specific route of the application, used primarily on the Bottom Navigation
 * @param name the name of the route
 * @param content the content of the route
 */
data class Route(val name: String, val content: String, val icon: ImageVector)
