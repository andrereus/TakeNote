package com.andre.takenote

import androidx.room.Entity
import androidx.room.PrimaryKey

// Database Model
// Entity defines a table in the database
// "@Entity" is an annotation that tells room what to do with the provided code
// "data class" is a class that provides special features like .toString() and more
@Entity
data class Note(
    // Primary key is needed as a unique identifier and will be auto generated for each entry
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val text: String
)
