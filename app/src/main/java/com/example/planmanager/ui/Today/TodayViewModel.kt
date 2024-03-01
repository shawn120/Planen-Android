package com.example.planmanager.ui.Today

import android.text.TextUtils
import android.util.Log
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.planmanager.R
import com.example.planmanager.data.Deadline

class TodayViewModel : ViewModel() {

    private var _deadlineItems = MutableLiveData<MutableList<Deadline>?>(null)
    val deadlineItems: LiveData<MutableList<Deadline>?> = _deadlineItems
    fun loadData(newDeadlineTitle: String){
        if (!TextUtils.isEmpty(newDeadlineTitle)) {
            val newDeadline = Deadline(
                newDeadlineTitle,
                "2024-03-07",
                "2024-02-22"
            )

            val currentList = _deadlineItems.value
            if (currentList == null) {
                _deadlineItems.value = mutableListOf(newDeadline)
            } else {
                _deadlineItems.value?.add(newDeadline)
            }
        }

    }
}