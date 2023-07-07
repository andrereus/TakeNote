package com.andre.takenote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Interestingly the automatically imported Material3 Components need experimental opt in
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(state: NoteState, onEvent: (NoteEvent) -> Unit) {
    // Interestingly there is no easy way to omit the paddingValues Parameter without warnings
    // from the IDE if it's not needed (even official example apps use @SuppressLint for it),
    // in this case it doesn't matter because I need it to push the content down for the TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TakeNote", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(NoteEvent.ShowDialog) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        }
    ) { padding ->
        if (state.isAddingNote) {
            AddNoteDialog(state = state, onEvent = onEvent)
        }

        // State only needed for dropdown expansion
        var expanded by remember { mutableStateOf(false) }

        // Because Column does not have contentPadding parameter like other components for some reason,
        // the PaddingValues that get passed from the Scaffold need to be set manually,
        // to push the content down the exact amount of space of the TopAppBar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, top = padding.calculateTopPadding(), end = 12.dp)
        ) {
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

                        // Interestingly Spacer needs an additional modifier to apply a
                        // default behavior of taking up space automatically
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.notes) { note ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = note.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Text(text = note.text, fontSize = 16.sp)
                            }

                            IconButton(onClick = { onEvent(NoteEvent.DeleteNote(note)) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete note"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}