package com.example.greatweek.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.greatweek.data.model.RetrofitCreateUser
import com.example.greatweek.data.model.RetrofitLoginUser
import com.example.greatweek.data.network.GreatWeekApiService
import kotlinx.coroutines.launch

class SettingsViewModel(private val greatWeekApi: GreatWeekApiService): ViewModel() {

    fun register(username: String, password: String, email: String) = viewModelScope.launch {
        val retrofitCreateUser = RetrofitCreateUser(username, password, email)
        try {
            greatWeekApi.register(retrofitCreateUser)
        } catch (e: Exception) {
            Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
        }
    }

    fun login(username: String, password: String) = viewModelScope.launch {
        val retrofitLoginUser = RetrofitLoginUser(username, password)
        try {
            greatWeekApi.login(retrofitLoginUser)
        } catch (e: Exception) {
            Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(
    private val greatWeekApi: GreatWeekApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(
                greatWeekApi = greatWeekApi
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}