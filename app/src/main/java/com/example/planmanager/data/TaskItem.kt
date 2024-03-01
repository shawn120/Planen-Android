package com.example.planmanager.data

import com.example.planmanager.util.TaskType

data class TaskItem(
    val taskType: TaskType,
    var deadline: Deadline? = null,
    var todo: ToDoItem? = null
)
