package com.example.planmanager.ui

import android.app.Application
import android.accessibilityservice.AccessibilityService.TakeScreenshotCallback
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.planmanager.data.AppDatabase
import com.example.planmanager.data.ToDoItem
import com.example.planmanager.data.Deadline
import com.example.planmanager.data.ScheduleItem
import com.example.planmanager.data.TaskItem
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

    fun loadDeadline(newDeadlineTitle: String, deadlineDate: String, startDate: String){
        if (!TextUtils.isEmpty(newDeadlineTitle) && !TextUtils.isEmpty(deadlineDate)) {
            val newDeadline = Deadline(
                newDeadlineTitle,
                deadlineDate,
                startDate
            )
            val newTask = TaskItem(
                taskType = TaskType.DEADLINE,
                deadline = newDeadline
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
    fun loadTodo(todoItem: ToDoItem) {
        var currentList = _taskItems.value
        val newTodo = TaskItem (
            taskType = TaskType.TODO,
            todo = todoItem
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

    fun loadSchedule(scheduleItem: ScheduleItem){
        var currentList = _taskItems.value
        val newScheduleTask = TaskItem (
            taskType = TaskType.SCHEDULE,
            schedule = scheduleItem
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