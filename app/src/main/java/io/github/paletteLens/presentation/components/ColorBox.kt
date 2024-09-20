package io.github.paletteLens.presentation.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import io.github.paletteLens.domain.model.color.ColorRepresentation
import io.github.paletteLens.util.ConvertToHexColor

@Composable
fun ColorBox(
    color: ColorRepresentation,
    modifier: Modifier = Modifier,
    detailed: Boolean = false,
) {
    val clipBoard = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val hex = ConvertToHexColor.correctString(color.hex)
    val intColor = android.graphics.Color.parseColor(hex)
    val composeColor = Color(intColor)

    val contrastWithBlack = ColorUtils.calculateContrast(android.graphics.Color.BLACK, intColor)
    val contrastWithWhite = ColorUtils.calculateContrast(android.graphics.Color.WHITE, intColor)
    val textColor = if (contrastWithBlack > contrastWithWhite) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.inverseOnSurface
    // uses black or white as the text color depending on the background contrast
    Box(
        modifier =
            modifier
                .background(composeColor),
        contentAlignment = Alignment.Center,
    ) {
        when (detailed) {
            true -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    color.name?.let { Text(text = it, color = textColor, style = MaterialTheme.typography.labelMedium) }
                    OutlinedButton(
                        modifier = modifier.size(24.dp, 24.dp),
                        onClick = {
                            val clipData = ClipData.newPlainText("label", hex)
                            clipBoard.setPrimaryClip(clipData)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar para área de transferência",
                        )
                    }
                }
            }
            false -> {
            }
        }
    }
}
