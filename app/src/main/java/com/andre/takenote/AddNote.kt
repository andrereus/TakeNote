package com.andre.takenote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(state: NoteState, onEvent: (NoteEvent) -> Unit) {
    AlertDialog(
        onDismissRequest = { onEvent(NoteEvent.HideDialog) },
        title = { Text(text = "Add note") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = state.title,
                    onValueChange = { onEvent(NoteEvent.SetTitle(it)) },
                    placeholder = { Text(text = "Title") }
                )

                // Because there is no separate component for a multiline text input,
                // the TextField needs to be modified in it's size and "maxLines" adjusted
                TextField(
                    value = state.text,
                    onValueChange = { onEvent(NoteEvent.SetText(it)) },
                    placeholder = { Text(text = "Text") },
                    maxLines = Int.MAX_VALUE,
                    modifier = Modifier.height(150.dp)
                )
            }
        },
        confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Button(onClick = { onEvent(NoteEvent.SaveNote) }) {
                    Text(text = "Save")
                }
            }
        }
    )
}