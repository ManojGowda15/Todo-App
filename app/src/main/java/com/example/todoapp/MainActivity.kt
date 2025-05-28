package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.todoapp.data.TodoDatabase
import com.example.todoapp.data.TodoRepository
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.ui.theme.TodoScreen
import com.example.todoapp.viewmodel.TodoViewModel
import com.example.todoapp.viewmodel.TodoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db =
            Room.databaseBuilder(
                applicationContext,
                TodoDatabase::class.java,
                "todo_db"
            ).build()

        val repository = TodoRepository(db.TodoDao())
        val factory = TodoViewModelFactory(repository)

        setContent {
            TodoAppTheme {
                val viewModel: TodoViewModel = viewModel(factory = factory)
                TodoScreen(viewModel)
            }
        }
    }
}

