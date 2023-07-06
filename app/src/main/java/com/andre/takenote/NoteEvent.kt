package com.andre.takenote

// User events for the ViewModel
sealed interface NoteEvent {
    object SaveNote : NoteEvent
    data class SetTitle(val title: String) : NoteEvent
    data class SetText(val text: String) : NoteEvent
    object ShowDialog : NoteEvent
    object HideDialog : NoteEvent
    data class SortNotes(val sortType: SortType) : NoteEvent
    data class DeleteNote(val note: Note) : NoteEvent
}