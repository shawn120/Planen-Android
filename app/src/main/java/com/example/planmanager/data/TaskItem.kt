package com.example.planmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.planmanager.util.TaskType

@Entity
data class TaskItem(
    @PrimaryKey val taskId: Int,
    val taskType: TaskType,
    var deadline: Deadline? = null,
    var todo: ToDoItem? = null,
    var schedule: ScheduleItem? = null,
)
