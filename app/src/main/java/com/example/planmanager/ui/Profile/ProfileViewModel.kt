package com.example.planmanager.ui.Profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Profile Fragment"
    }
    val text: LiveData<String> = _text

    data class UserData(
        val name: String?,
        val email: String?,
        val photoUrl: String?
    )

    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData>  = _userData

    fun updateUserData(name: String?, email: String?, photoUrl: String?) {
        _userData.value = UserData(name, email, photoUrl)
    }
}