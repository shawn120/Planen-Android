package com.example.planmanager.ui

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.planmanager.data.TaskItem
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.planmanager.data.AppDatabase
import com.example.planmanager.data.TaskItemLocalRepository
import com.example.planmanager.util.TaskType
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application){

    private var _taskItems = MutableLiveData<MutableList<TaskItem>?>(null)
    val taskItems: LiveData<MutableList<TaskItem>?> = _taskItems
    private val repository = TaskItemLocalRepository(
        AppDatabase.getInstance(application).taskItemDao()
    )

    val taskItemLocals = repository.getAllLocalTaskItem().asLiveData()

    val taskItemLocalsToday = repository.getAllLocalTaskItemToday().asLiveData()

    fun updateTodoCompletion(taskId: String, completed: Boolean) {
        viewModelScope.launch {
            val taskItem = repository.getLocalTaskItem(taskId)
            if (taskItem != null && taskItem.taskType == TaskType.TODO) {
                taskItem.completedToDo = completed
                repository.updateTaskItem(taskItem)
            }
        }
    }
    fun getTaskOnDay(date:String) : LiveData<MutableList<TaskItem>?>{
        val list = repository.getAllLocalTaskItemOnDay(date).asLiveData()
        return list
    }

    fun deleteTask(task: TaskItem){
        viewModelScope.launch {
            repository.deleteTaskItem(task)
        }
    }

    fun updateTask(task: TaskItem) {
        viewModelScope.launch {
            repository.updateTaskItem(task)
        }
    }
    fun loadDeadline(newDeadlineTitle: String, deadlineDate: String, startDate: String){
        if (!TextUtils.isEmpty(newDeadlineTitle) && !TextUtils.isEmpty(deadlineDate)) {
            val newTask = TaskItem(
                isDeadline = true,
                title = newDeadlineTitle,
                dateDeadline = deadlineDate,
                startDateDeadline = startDate,
            )
            var currentList = _taskItems.value
            if (currentList == null) {

                currentList = mutableListOf(newTask)
            } else {
                currentList.add(0, newTask)
            }
            _taskItems.value = currentList
            viewModelScope.launch {
                repository.insertTaskItem(newTask)
            }
        }

    }
    fun loadTodo(newToDoTitle: String, newToDoDate: String) {
        var currentList = _taskItems.value
        val newTodo = TaskItem (
            isToDo = true,
            title = newToDoTitle,
            dateToDo = newToDoDate,
        )
        if (currentList == null) {
            currentList = mutableListOf(newTodo)
        } else {
            currentList.add(0,newTodo)
        }
        _taskItems.value = currentList
        viewModelScope.launch {
            repository.insertTaskItem(newTodo)
        }
    }

    fun loadSchedule(newScheduleTitle: String, newScheduleLocation: String, newScheduleDate: String,
                     newScheduleTime: String){
        var currentList = _taskItems.value
        val newScheduleTask = TaskItem (
            isSchedule = true,
            title = newScheduleTitle,
            locationSchedule = newScheduleLocation,
            dateSchedule = newScheduleDate,
            timeSchedule = newScheduleTime
        )
        if (currentList == null) {
            currentList = mutableListOf(newScheduleTask)
        } else {
            currentList.add(0,newScheduleTask)
        }
        _taskItems.value = currentList
        viewModelScope.launch {
            repository.insertTaskItem(newScheduleTask)
        }
    }
}