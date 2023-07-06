package com.andre.takenote

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(state: NoteState, onEvent: (NoteEvent) -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(NoteEvent.ShowDialog) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        }
    ) { _ ->
        if (state.isAddingNote) {
            AddNoteDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.values().forEach { sortType ->
                        Row(
                            modifier = Modifier.clickable { onEvent(NoteEvent.SortNotes(sortType)) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.sortType == sortType,
                                onClick = { onEvent(NoteEvent.SortNotes(sortType)) }
                            )
                            Text(text = sortType.name)
                        }
                    }
                }
            }
            items(state.notes) { note ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = note.title, fontSize = 20.sp)
                        Text(text = note.text, fontSize = 12.sp)
                    }
                    IconButton(onClick = { onEvent(NoteEvent.DeleteNote(note)) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete note")
                    }
                }
            }
        }
    }
}