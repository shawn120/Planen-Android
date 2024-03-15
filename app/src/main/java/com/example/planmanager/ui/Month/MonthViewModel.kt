package com.example.planmanager.ui.Month

import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.planmanager.ui.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MonthViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        val currentDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate.time)
        value = formattedDate
    }
    val text: LiveData<String> = _text
}