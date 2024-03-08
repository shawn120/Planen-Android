package com.example.planmanager.data

data class ToDoItem(
    var text: String,
    var date: String? = null, //for edit date, choose date on calendar(month)
    var completed: Boolean = false

)

