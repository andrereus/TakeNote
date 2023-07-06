package com.andre.takenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.andre.takenote.ui.theme.TakeNoteTheme

class MainActivity : ComponentActivity() {
    // To avoid initializing the database here, use dependency injection like Dagger Hilt
    // (Recommended for bigger apps / for production)
    private val db by lazy {
        Room.databaseBuilder(applicationContext, NoteDatabase::class.java, "notes.db").build()
    }

    // To avoid using a factory, also use something like Dagger Hilt
    private val viewModel by viewModels<NoteViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NoteViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TakeNoteTheme {
                val state by viewModel.state.collectAsState()
                NoteScreen(state = state, onEvent = viewModel::onEvent)
            }
        }
    }
}