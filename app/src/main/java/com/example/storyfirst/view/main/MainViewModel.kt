package com.example.storyfirst.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyfirst.helper.PreferencesHelper
import com.example.storyfirst.model.UserModel
import com.example.storyfirst.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: PreferencesHelper) : ViewModel() {
//    fun getUser(): LiveData<UserModel> {
//        return pref.getUser().asLiveData()
//    }

    fun logout() {
        viewModelScope.launch {
            pref.clear()
        }
    }

}