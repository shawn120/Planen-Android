package com.example.planmanager

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