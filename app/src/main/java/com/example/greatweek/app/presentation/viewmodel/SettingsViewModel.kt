package com.example.greatweek.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.greatweek.R
import com.example.greatweek.data.model.RetrofitCreateUser
import com.example.greatweek.data.model.RetrofitLoginUser
import com.example.greatweek.data.network.GreatWeekApi
import kotlinx.coroutines.launch

class SettingsViewModel(private val greatWeekApi: GreatWeekApi): ViewModel() {

    private var _isAuthorised = MutableLiveData(false)
    val isAuthorised: LiveData<Boolean> get() = _isAuthorised

    fun register(username: String, password: String, email: String) = viewModelScope.launch {
        val retrofitCreateUser = RetrofitCreateUser(username, password, email)
        try {
            greatWeekApi.register(retrofitCreateUser)
            // TODO получить ответ
            // TODO если удачно -> сохранить логин и пароль -> login(username, password)
        } catch (e: Exception) {
            Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
        }
    }

    fun login(username: String, password: String) = viewModelScope.launch {
        val retrofitLoginUser = RetrofitLoginUser(username, password)
        try {
            greatWeekApi.login(retrofitLoginUser)
            // TODO получить ответ
            // TODO если удачно -> сохранить токен -> _isAuthorised.value = true
        } catch (e: Exception) {
            Log.e("TAG", "Exception during request -> ${e.localizedMessage}")
        }
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
    private val greatWeekApi: GreatWeekApi
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