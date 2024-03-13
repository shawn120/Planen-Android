package com.example.planmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.planmanager.util.TaskType
import java.io.Serializable
import java.util.UUID

@Entity
data class TaskItem(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    @Transient
    var taskType: TaskType,
    @Transient
    var deadline: Deadline? = null,
    @Transient
    var todo: ToDoItem? = null,
    @Transient
    var schedule: ScheduleItem? = null,
)