package com.example.todoapp.data

import kotlinx.coroutines.flow.Flow

class TodoRepository(private val dao: TodoDao) {
    val allTodos: Flow<List<Todo>> = dao.getAllTodos()

    suspend fun addTodo(todo: Todo) = dao.insertTodo(todo)
    suspend fun deleteTodo(todo: Todo) = dao.deleteTodo(todo)
    suspend fun updateTodo(todo: Todo) = dao.updateTodo(todo)
}