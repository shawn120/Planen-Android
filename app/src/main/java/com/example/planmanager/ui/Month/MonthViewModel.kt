package com.example.planmanager.ui.Month

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MonthViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = " "
    }
    val text: LiveData<String> = _text
}