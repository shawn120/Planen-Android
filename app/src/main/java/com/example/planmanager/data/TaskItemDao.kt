package com.example.planmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskItemDao {
    @Insert
    suspend fun insert(task: TaskItem)

    @Delete
    suspend fun delete(task: TaskItem)

    @Query("SELECT * FROM TaskItem")
    fun getAllLocalTasks() : Flow<MutableList<TaskItem>?>
}