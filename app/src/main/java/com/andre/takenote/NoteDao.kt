package com.andre.takenote

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

// Data Access Object
// Defines how to interact with the data in the database
// "interface" defines which variables and methods a class needs to implement
@Dao
interface NoteDao {
    // "@Upsert" updates if note already exists and inserts if it doesn't exist
    // (no conflict strategy needed with @Insert)
    // "suspend" is for coroutines/asynchronous tasks (no callbacks needed)
    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    // Room doesn't provide "read" or "get", so it needs to be implemented with @Query
    // Flow is used to get realtime updates as soon as something changes in the database
    // (reactive streams / observables)
    @Query("SELECT * FROM note ORDER BY id ASC")
    fun getNotesOrderedById(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY title ASC")
    fun getNotesOrderedByTitle(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY text ASC")
    fun getNotesOrderedByText(): Flow<List<Note>>
}