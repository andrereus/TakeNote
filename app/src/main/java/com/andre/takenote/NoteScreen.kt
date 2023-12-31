package com.andre.takenote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Automatically imported Material3 Components still need experimental opt in
// State is passed into Component and Events are hoisted to ViewModel (with Lambda)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(state: NoteState, onEvent: (NoteEvent) -> Unit) {
    // There is no easy way to omit the paddingValues Parameter without warnings
    // from the IDE if it's not needed (even official example apps use @SuppressLint for it)
    // In this case it doesn't matter, because I need it to push the content down for the TopAppBar
    Scaffold(
        topBar = {
            // Background color is adjusted to the app theme to match notification bar etc.
            // Text color matching background color is used (white text on dark background)
            TopAppBar(
                title = { Text("TakeNote", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            // Android-typical button on the bottom right
            // onClick triggers onEvent in the ViewModel, which sets isAddingNote
            // from NoteState to true (which opens the AddNote Dialog)
            FloatingActionButton(onClick = { onEvent(NoteEvent.ShowDialog) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        }
    ) { padding ->
        // Show Dialog based on isAddingNote boolean coming from NoteState, passed through ViewModel
        // (First it's in the mutable "_state" which is then combined in immutable "state")
        // In the MainActivity it's passed from ViewModel to NoteScreen
        if (state.isAddingNote) {
            AddNote(state = state, onEvent = onEvent)
        }

        // Because Column does not have contentPadding parameter like other components like LazyColumn,
        // the PaddingValues that get passed from the Scaffold need to be set manually,
        // to push the content down the exact amount of space of the TopAppBar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, top = padding.calculateTopPadding(), end = 12.dp)
        ) {
            // Sorting Dialog in separate component (again with passing state and onEvent Lambda)
            SortNotes(state = state, onEvent = onEvent)

            // LazyColumn for RecyclerView (only composes visible items)
            // Arrangement.spacedBy is not optimal and spaceEvenly can't be invoked etc.
            // Bottom padding on the Card is used instead
            // This also makes sure that the last element has a spacing at the end
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp)
            ) {
                // Iterate through list of notes and put out cards with content and delete button
                items(state.notes) { note ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            // Modifier.weight(1f) takes up all the space it can get
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