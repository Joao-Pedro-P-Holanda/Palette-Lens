package io.github.paletteLens.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.paletteLens.domain.model.color.Palette

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PaletteCard(
    modifier: Modifier = Modifier,
    palette: Palette,
) {
    ElevatedCard(
        modifier =
            modifier
                .padding(4.dp, 4.dp)
                .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
    ) {
        Text(
            modifier = modifier.padding(8.dp),
            text = palette.metadata.name ?: "Paleta sem nome",
            style = MaterialTheme.typography.titleLarge,
        )
        FlowRow(horizontalArrangement = Arrangement.SpaceAround, modifier = modifier.padding(8.dp).fillMaxWidth()) {
            Text(text = "Número de cores: ${palette.metadata.numberOfColors}", style = MaterialTheme.typography.bodyLarge)
            palette.colors.forEach {
                ColorBox(modifier = modifier.size(20.dp), color = it)
            }
        }
    }
}
