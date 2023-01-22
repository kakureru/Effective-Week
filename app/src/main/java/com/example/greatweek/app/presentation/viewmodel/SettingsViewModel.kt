package com.example.greatweek.app.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
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

    private var _loginState = MutableLiveData(
        if (!sharedPreferences.getBoolean(AUTH_STATUS_KEY, false))
            LoginState.UNAUTHORIZED
        else
            LoginState.AUTHORIZED
    )
    val loginState: LiveData<LoginState> get() = _loginState

    private var _username: MutableLiveData<String> = MutableLiveData(sharedPreferences.getString(USERNAME_KEY, ""))
    val username: LiveData<String> get() = _username

    fun register(username: String, password: String, email: String) = viewModelScope.launch {
        val remoteUser = RemoteUser(username = username, password = password, email = email)
        greatWeekApi.register(remoteUser).onSuccess {
            Log.d("TAG", "Reg response -> $it")
            login(username, password)
        }
            .onFailure {
                _loginState.value = LoginState.FAILURE(message = it.localizedMessage)
            }
    }

    fun login(username: String, password: String) = viewModelScope.launch {
        val remoteUserLogin = RemoteUserLogin(username = username, password = password)

        greatWeekApi.login(remoteUserLogin).onSuccess {
            Log.d("TAG", "Login response -> $it")
            editor.apply {
                putBoolean(AUTH_STATUS_KEY, true)
                putString(AUTH_TOKEN_KEY, it.token)
                putString(USERNAME_KEY, username)
                apply()
            }
            _username.value = username
            _loginState.value = LoginState.AUTHORIZED
        }
            .onFailure {
                _loginState.value = LoginState.FAILURE(message = it.localizedMessage)
            }
    }

    fun logout() {
        editor.apply {
            putBoolean(AUTH_STATUS_KEY, false)
            remove(AUTH_TOKEN_KEY)
            remove(USERNAME_KEY)
            apply()
        }
        _loginState.value = LoginState.UNAUTHORIZED
    }

    /**
     * Reg data verification
     */

    fun checkRegistrationForm(
        username: String, email: String, password: String, passwordRepeat: String
    ): Pair<Boolean, Int> {
        if (username.isBlank())
            return Pair(false, com.example.greatweek.R.string.username_message)
        if (email.isBlank())
            return Pair(false, com.example.greatweek.R.string.email_message)
        if (password.isBlank())
            return Pair(false, com.example.greatweek.R.string.password_message)
        if (passwordRepeat.isBlank())
            return Pair(false, com.example.greatweek.R.string.password_repeat_message)
        if (password != passwordRepeat)
            return Pair(false, com.example.greatweek.R.string.passwords_do_not_match_message)
        else
            return Pair(true, com.example.greatweek.R.string.success_message)
    }

    /**
     * Auth data verification
     */

    fun checkAuthorisationForm(
        username: String, password: String
    ): Pair<Boolean, Int> {
        if (username.isBlank())
            return Pair(false, com.example.greatweek.R.string.username_message)
        if (password.isBlank())
            return Pair(false, com.example.greatweek.R.string.password_message)
        else
            return Pair(true, com.example.greatweek.R.string.success_message)
    }
}

sealed class LoginState {
    object UNAUTHORIZED : LoginState()
    object LOADING : LoginState()
    object AUTHORIZED : LoginState()
    data class FAILURE(val message: String?) : LoginState()
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