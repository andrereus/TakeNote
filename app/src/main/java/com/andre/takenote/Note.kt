package com.andre.takenote

import androidx.room.Entity
import androidx.room.PrimaryKey

// Database Model
// Entity defines a table in the database
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val text: String
)
