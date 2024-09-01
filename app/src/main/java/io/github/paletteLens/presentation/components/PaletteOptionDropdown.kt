package io.github.paletteLens.presentation.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PaletteOptionDropdown(
    modifier: Modifier = Modifier,
    dropdownItems: List<String> = listOf("Padrão"),
    onItemClick: (String) -> Unit,
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedItem by rememberSaveable {
        mutableStateOf(dropdownItems.first())
    }

    ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = {
        isExpanded = !isExpanded
    }) {
        TextField(
            readOnly = true,
            value = selectedItem,
            onValueChange = { },
            label = { Text("Formato da Paleta") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded,
                )
            },
            modifier = modifier.menuAnchor(),
        )
        ExposedDropdownMenu(
            modifier = modifier,
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            dropdownItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemClick(item)
                        selectedItem = item
                        isExpanded = false
                    },
                )
            }
        }
    }
}
