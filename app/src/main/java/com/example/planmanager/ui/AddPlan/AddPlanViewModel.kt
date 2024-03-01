package com.example.planmanager.ui.AddPlan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddPlanViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Add Plan Fragment"
    }
    val text: LiveData<String> = _text
}