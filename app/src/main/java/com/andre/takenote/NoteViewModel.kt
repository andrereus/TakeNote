package com.andre.takenote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel
// UI elements pass events to ViewModel and ViewModel passes state to UI elements
// Pass in DAO to interact with the database
class NoteViewModel(
    private val dao: NoteDao
): ViewModel() {
    // Create MutableStateFlow from NoteState.kt
    // Hosts state for UI
    private val _state = MutableStateFlow(NoteState())

    // Trigger on events in the UI
    fun onEvent(event: NoteEvent) {
        // Check which event
        when(event) {
            // Shortcut: Because of NoteEvent.kt you can press "option + enter" to add from it
            is NoteEvent.DeleteNote -> {
                // Create coroutine scope because deleteNote() is asynchronous (suspended)
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }
            NoteEvent.HideDialog -> {
                // "update" feature is coming from MutableStateFlow
                // "copy" creates a copy with a new NoteState where only isAddingNote is different
                _state.update { it.copy(
                    isAddingNote = false
                ) }
            }
            NoteEvent.SaveNote -> TODO()
            is NoteEvent.SetText -> TODO()
            is NoteEvent.SetTitle -> TODO()
            NoteEvent.ShowDialog -> TODO()
        }
    }
}