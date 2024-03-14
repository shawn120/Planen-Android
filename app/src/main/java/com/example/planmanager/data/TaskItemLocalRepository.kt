package com.example.planmanager.data

class TaskItemLocalRepository(
    private val dao: TaskItemDao
) {
    suspend fun insertTaskItem(task: TaskItem) = dao.insert(task)

    suspend fun deleteTaskItem(task: TaskItem) = dao.delete(task)

    fun getAllLocalTaskItem() = dao.getAllLocalTasks()

    fun getAllLocalTaskItemToday() = dao.getAllLocalTasksToday()
}