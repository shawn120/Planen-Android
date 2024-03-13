package com.example.planmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.planmanager.util.TaskType
import java.util.UUID

@Entity
data class TaskItem(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val taskType: TaskType,
    var deadline: Deadline? = null,

    var todo: ToDoItem? = null,
    var schedule: ScheduleItem? = null,
)
