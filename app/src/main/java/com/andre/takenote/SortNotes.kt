package com.andre.takenote

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortNotes(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit
) {
    // State only needed for dropdown expansion
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(top = 12.dp)) {
        // Because there is no default dropdown as it should be,
        // it needs to be constructed with a button
        // (an implementation with a TextField is not a good practice in my opinion)
        Button(onClick = { expanded = true }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (state.sortType == SortType.ID) {
                    Text(text = "Default sort order")
                } else {
                    Text(text = "Sorted by ${state.sortType.name.lowercase()}")
                }

                // Spacer to avoid adding Modifier.weight(1f) to every Text above
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Open Dropdown"
                )
            }
        }

        // DropdownMenu is a popup and can't easily be controlled from the rest of the layout
        // and padding can't be applied to the outside of the component,
        // not even from outer components
        // Because of this fillMaxWidth is reduced by a fraction,
        // to at least add some space to the edge
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.93f)
        ) {
            SortType.values().forEach { sortType ->
                DropdownMenuItem(
                    text = {
                        if (sortType == SortType.ID) {
                            Text(text = "Default sort order")
                        } else {
                            Text(text = "Sort by ${sortType.name.lowercase()}")
                        }
                    },
                    onClick = {
                        onEvent(NoteEvent.SortNotes(sortType))
                        expanded = false
                    })
            }
        }
    }
}