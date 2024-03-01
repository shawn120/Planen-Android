package com.example.planmanager

import com.example.planmanager.data.ToDoItem

class DummyTodoList {

    companion object {
        fun getDummyTodoList(): List<ToDoItem> {
            return listOf(
                ToDoItem( "finish assignment3"),
                ToDoItem( "demo assignment3"),
                ToDoItem( "sleep"),
                ToDoItem( "sleep"),
                ToDoItem( "sleep"),
                ToDoItem("sleep"),
                ToDoItem("sleep"),
                ToDoItem( "sleep")
            )
        }
    }
}