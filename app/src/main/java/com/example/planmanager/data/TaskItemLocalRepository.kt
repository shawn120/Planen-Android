package com.example.planmanager.data

class TaskItemLocalRepository(
    private val dao: TaskItemDao
) {
    suspend fun insertTaskItem(task: TaskItem) = dao.insert(task)

    suspend fun deleteTaskItem(task: TaskItem) = dao.delete(task)

    suspend fun updateTaskItem(task: TaskItem) = dao.update(task)

    suspend fun getLocalTaskItem(taskId: String) = dao.getLocalTaskItem(taskId)

    suspend fun getLocalTaskItemByName(taskName: String) = dao.getLocalTaskItemByName(taskName)

    fun getAllLocalTaskItem() = dao.getAllLocalTasks()

    fun getAllLocalTaskItemToday() = dao.getAllLocalTasksToday()

    fun getAllLocalTaskItemTodayWithRange(rangeString: String) = dao.getAllLocalTasksTodayWithRange(rangeString)

    fun getAllLocalTaskItemOnDay(date: String) = dao.getAllLocalTasksOnDate(date)
}