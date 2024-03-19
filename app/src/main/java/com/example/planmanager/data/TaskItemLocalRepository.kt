package com.example.planmanager.data

class TaskItemLocalRepository(
    private val dao: TaskItemDao
) {
    suspend fun insertTaskItem(task: TaskItem) = dao.insert(task)

    suspend fun deleteTaskItem(task: TaskItem) = dao.delete(task)

    suspend fun updateTaskItem(task: TaskItem) = dao.update(task)

    fun getAllLocalTaskItem() = dao.getAllLocalTasks()

    fun getAllLocalTaskItemToday(range: Int) = dao.getAllLocalTasksToday(range)

    fun getAllLocalTaskItemOnDay(date: String) = dao.getAllLocalTasksOnDate(date)
}