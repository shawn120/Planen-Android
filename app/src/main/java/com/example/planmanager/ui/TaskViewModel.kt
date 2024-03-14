package com.example.planmanager.ui

import android.app.Application
import android.accessibilityservice.AccessibilityService.TakeScreenshotCallback
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.planmanager.data.TaskItem
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.planmanager.data.AppDatabase
import com.example.planmanager.data.TaskItemLocalRepository
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
            val newTask = TaskItem(
                isDeadline = true,
                titleDeadline = newDeadlineTitle,
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
            titleToDo = newToDoTitle,
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
            titleSchedule = newScheduleTitle,
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