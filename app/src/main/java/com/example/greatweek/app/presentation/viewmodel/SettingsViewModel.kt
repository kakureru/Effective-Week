package com.example.greatweek.app.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.example.greatweek.R
import com.example.greatweek.app.presentation.constants.AUTH_STATUS_KEY
import com.example.greatweek.app.presentation.constants.AUTH_TOKEN_KEY
import com.example.greatweek.app.presentation.constants.USERNAME_KEY
import com.example.greatweek.data.model.RemoteUser
import com.example.greatweek.data.model.RemoteUserLogin
import com.example.greatweek.data.network.GreatWeekApi
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val greatWeekApi: GreatWeekApi,
    private val sharedPreferences: SharedPreferences
    ): ViewModel() {

    private val editor = sharedPreferences.edit()

    private var _isAuthorised = MutableLiveData(sharedPreferences.getBoolean(AUTH_STATUS_KEY, false))
    val isAuthorised: LiveData<Boolean> get() = _isAuthorised

    private var _username: MutableLiveData<String> = MutableLiveData(sharedPreferences.getString(USERNAME_KEY, ""))
    val username: LiveData<String> get() = _username

    fun register(username: String, password: String, email: String) = viewModelScope.launch {
        val remoteUser = RemoteUser(username = username, password = password, email = email)
        try {
            val response = greatWeekApi.register(remoteUser)
            Log.d("TAG", "Reg response -> $response")
            login(username, password)
        } catch (e: Exception) {
            Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
        }
    }

    fun login(username: String, password: String) = viewModelScope.launch {
        val remoteUserLogin = RemoteUserLogin(username = username, password = password)
        try {
            val response = greatWeekApi.login(remoteUserLogin)
            Log.d("TAG", "Login response -> $response")
            editor.apply {
                putBoolean(AUTH_STATUS_KEY, true)
                putString(AUTH_TOKEN_KEY, response.token)
                putString(USERNAME_KEY, username)
                apply()
            }
            _username.value = username
            _isAuthorised.value = true
        } catch (e: Exception) {
            Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
        }
    }

    fun logout() {
        editor.apply {
            putBoolean(AUTH_STATUS_KEY, false)
            remove(AUTH_TOKEN_KEY)
            remove(USERNAME_KEY)
            apply()
        }
        _isAuthorised.value = false
    }

    /**
     * Reg data verification
     */

    fun checkRegistrationForm(
        username: String, email: String, password: String, passwordRepeat: String
    ): Pair<Boolean, Int> {
        if (username.isBlank())
            return Pair(false, R.string.username_message)
        if (email.isBlank())
            return Pair(false, R.string.email_message)
        if (password.isBlank())
            return Pair(false, R.string.password_message)
        if (passwordRepeat.isBlank())
            return Pair(false, R.string.password_repeat_message)
        if (password != passwordRepeat)
            return Pair(false, R.string.passwords_do_not_match_message)
        else
            return Pair(true, R.string.success_message)
    }

    /**
     * Auth data verification
     */

    fun checkAuthorisationForm(
        username: String, password: String
    ): Pair<Boolean, Int> {
        if (username.isBlank())
            return Pair(false, R.string.username_message)
        if (password.isBlank())
            return Pair(false, R.string.password_message)
        else
            return Pair(true, R.string.success_message)
    }
}

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(
    private val greatWeekApi: GreatWeekApi,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(
                greatWeekApi = greatWeekApi,
                sharedPreferences = sharedPreferences
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}