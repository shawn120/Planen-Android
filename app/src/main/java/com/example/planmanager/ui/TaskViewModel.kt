package com.example.planmanager.ui

import android.app.Application
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.planmanager.data.TaskItem
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.planmanager.data.AppDatabase
import com.example.planmanager.data.HolidayItem
import com.example.planmanager.data.HolidayRepository
import com.example.planmanager.data.HolidayService
import com.example.planmanager.data.TaskItemLocalRepository
import com.example.planmanager.util.TaskType
import kotlinx.coroutines.launch
import androidx.preference.PreferenceManager
import com.example.planmanager.R

class TaskViewModel(application: Application) : AndroidViewModel(application){

    private var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    private var _taskItems = MutableLiveData<MutableList<TaskItem>?>(null)
    val taskItems: LiveData<MutableList<TaskItem>?> = _taskItems
    private var taskUpdateListener: TaskUpdateListener? = null
    private val repository = TaskItemLocalRepository(
        AppDatabase.getInstance(application).taskItemDao()
    )

    private val holidayRepository = HolidayRepository(HolidayService.create())

    val taskItemLocals = repository.getAllLocalTaskItem().asLiveData()

    var taskItemLocalsToday = repository.getAllLocalTaskItemToday().asLiveData()

    var range = prefs.getString(
        getApplication<Application>().getString(R.string.pref_range_key),
        getApplication<Application>().getString(R.string.pref_range_default_value)
    )

//    var taskItemLocalsTodayWithRange = repository.getAllLocalTaskItemTodayWithRange(
//        range?:"+0 day"
//    ).asLiveData()

//    var taskItemLocalsTodayWithRange = repository.getAllLocalTaskItemTodayWithRange(
//        "+0 day"
//    ).asLiveData()


    private val _apiError = MutableLiveData<Throwable?>(null)
    val apiError : LiveData<Throwable?> = _apiError

    private val _apiResult = MutableLiveData<MutableList<HolidayItem>?>(null)
    val apiResult: LiveData<MutableList<HolidayItem>?> = _apiResult

    fun getListWithRange(range: String): LiveData<MutableList<TaskItem>?>{
        val taskItemLocalsTodayWithRange = repository.getAllLocalTaskItemTodayWithRange(range).asLiveData()
        return taskItemLocalsTodayWithRange
    }

    fun updateHoliday(holidays: MutableList<HolidayItem>) {
        for (holiday in holidays) {
            val holidayName = holiday.name
            Log.d("getTASK", "$holidayName")
            viewModelScope.launch {
                val taskItem = repository.getLocalTaskItemByName(holidayName)
                Log.d("getTASK", "$taskItem")
                if (taskItem == null){
                    loadSchedule(
                        holiday.name,
                        "Holiday",
                        holiday.date,
                        ""
                    )
                }
            }
        }

    }
    fun updateTodoCompletion(taskId: String, completed: Boolean) {
        viewModelScope.launch {
            val taskItem = repository.getLocalTaskItem(taskId)
            if (taskItem != null && taskItem.taskType == TaskType.TODO) {
                taskItem.completedToDo = completed
                repository.updateTaskItem(taskItem)

                taskUpdateListener?.onTaskUpdateCompleted()
                taskItemLocalsToday = repository.getAllLocalTaskItemToday().asLiveData()
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

    fun loadHoliday(
        year: String,
        countryCode: String
    ) {
        viewModelScope.launch{
            val result = holidayRepository.loadHoliday(year, countryCode)
            _apiError.value = result.exceptionOrNull()
            _apiResult.value = result.getOrNull()?.toMutableList()
            Log.d("APIinViewModel", "${_apiError.value}")
            Log.d("APIinViewModel", "${_apiResult.value}")
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
    interface TaskUpdateListener {
        fun onTaskUpdateCompleted()
    }
    fun setTaskUpdateListener(listener: TaskUpdateListener) {
        taskUpdateListener = listener
    }
}