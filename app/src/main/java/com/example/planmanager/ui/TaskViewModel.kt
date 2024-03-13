package com.example.planmanager.ui

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.planmanager.data.TaskItem

class TaskViewModel : ViewModel() {

    private var _taskItems = MutableLiveData<MutableList<TaskItem>?>(null)
    val taskItems: LiveData<MutableList<TaskItem>?> = _taskItems

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
    }
}