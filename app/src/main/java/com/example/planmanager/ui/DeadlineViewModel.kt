package com.example.planmanager.ui

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.planmanager.data.Deadline

class DeadlineViewModel : ViewModel() {

    private var _deadlineItems = MutableLiveData<MutableList<Deadline>?>(null)
    val deadlineItems: LiveData<MutableList<Deadline>?> = _deadlineItems
    fun loadData(newDeadlineTitle: String, deadlineDate: String){
        if (!TextUtils.isEmpty(newDeadlineTitle) && !TextUtils.isEmpty(deadlineDate)) {
            val newDeadline = Deadline(
                newDeadlineTitle,
                deadlineDate,
                "2024-02-22"
            )

            var currentList = _deadlineItems.value
            if (currentList == null) {
                currentList = mutableListOf(newDeadline)
            } else {
                currentList.add(0, newDeadline)
            }
            _deadlineItems.value = currentList
        }

    }
}