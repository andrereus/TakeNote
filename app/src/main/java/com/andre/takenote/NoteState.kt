package com.andre.takenote

// State for the ViewModel
data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val text: String = "",
    val isAddingNote: Boolean = false,
    val sortType: SortType = SortType.ID
)
