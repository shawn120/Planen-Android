package com.example.planmanager.ui

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.planmanager.data.Deadline
import com.example.planmanager.data.TaskItem
import com.example.planmanager.util.TaskType

class TaskViewModel : ViewModel() {

    private var _taskItems = MutableLiveData<MutableList<TaskItem>?>(null)
    val taskItems: LiveData<MutableList<TaskItem>?> = _taskItems
    fun loadDeadline(newDeadlineTitle: String, deadlineDate: String){
        if (!TextUtils.isEmpty(newDeadlineTitle) && !TextUtils.isEmpty(deadlineDate)) {
            val newDeadline = Deadline(
                newDeadlineTitle,
                deadlineDate,
                "2024-02-22"
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
        }

    }
}