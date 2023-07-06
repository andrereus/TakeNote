package com.andre.takenote

import androidx.room.Database
import androidx.room.RoomDatabase

// Connect Entity and DAO for Room to use
// (No migrations for now, use in bigger apps / for production)
@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract val dao: NoteDao
}