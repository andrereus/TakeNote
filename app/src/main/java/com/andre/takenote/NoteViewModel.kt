package com.andre.takenote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel
// UI elements pass events to ViewModel and ViewModel passes state to UI elements
// Pass in DAO to interact with the database
class NoteViewModel(
    private val dao: NoteDao
): ViewModel() {
    private val _sortType = MutableStateFlow(SortType.ID)

    // Flow specific notation for the "reactive" sorting feature
    // "flatMapLatest" is comparable to RxJS / Observables in the Angular Framework
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _notes = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.ID -> dao.getNotesOrderedById()
                SortType.TITLE -> dao.getNotesOrderedByTitle()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Host state for UI
    // Create MutableStateFlow from NoteState.kt
    private val _state = MutableStateFlow(NoteState())

    // State that the UI will observe
    // (because state needs to be immutable, the "_state" from above is mutable)
    // "combine" is comparable to RxJS
    // Combines multiple Flows into one (in Angular it would be multiple Observers combined into one)
    val state = combine(_state, _sortType, _notes) { state, sortType, notes ->
        state.copy(
            notes = notes,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

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
            is NoteEvent.SetText -> {
                _state.update { it.copy(
                    text = event.text
                ) }
            }
            is NoteEvent.SetTitle -> {
                _state.update { it.copy(
                    title = event.title
                ) }
            }
            NoteEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingNote = true
                ) }
            }
            is NoteEvent.SortNotes -> {
                _sortType.value = event.sortType
            }
        }
    }
}