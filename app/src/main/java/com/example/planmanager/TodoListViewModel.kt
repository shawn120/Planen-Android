package com.example.planmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodoListViewModel : ViewModel() {
    private val _todoItems = MutableLiveData<List<ToDoItem>?>(null)
    val todoItems: LiveData<List<ToDoItem>?> = _todoItems

    init {
        // Initialize the todoItems LiveData with an empty list
        _todoItems.value = mutableListOf()
    }
    fun addTodoItem(todoItem: ToDoItem) {
        val currentList = _todoItems.value?.toMutableList() ?: mutableListOf()
        currentList.add(todoItem)
        _todoItems.value = currentList.toList()
    }
}