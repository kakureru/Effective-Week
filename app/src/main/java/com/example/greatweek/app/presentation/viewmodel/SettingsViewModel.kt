package com.example.greatweek.app.presentation.viewmodel

import androidx.lifecycle.*
import com.example.greatweek.R
import com.example.greatweek.app.presentation.utils.RequestState
import com.example.greatweek.domain.model.network.SignIn
import com.example.greatweek.domain.model.network.SignUp
import com.example.greatweek.domain.model.network.UserSignIn
import com.example.greatweek.domain.model.network.UserSignUp
import com.example.greatweek.domain.repository.UserRepository
import com.example.greatweek.domain.usecase.authentication.SignInUseCase
import com.example.greatweek.domain.usecase.authentication.SignUpUseCase
import com.example.greatweek.domain.utils.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepository,
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    ): ViewModel() {

    val authState: LiveData<Boolean> = userRepository.isAuthorised.asLiveData()
    val username: LiveData<String> = userRepository.username.asLiveData()

    private val _signInRequestState = MutableStateFlow<RequestState<SignIn>>(RequestState.Idle())
    val signInRequestState = _signInRequestState.asStateFlow()

    private val _signUpRequestState = MutableStateFlow<RequestState<SignUp>>(RequestState.Idle())
    val signUpRequestState = _signUpRequestState.asStateFlow()

    fun signIn(userSignIn: UserSignIn) = signInUseCase(userSignIn).collectRequest(_signInRequestState)

    fun signUp(userSignUp: UserSignUp) =
        viewModelScope.launch(Dispatchers.IO) {
            signUpUseCase(userSignUp).collect {
                when (it) {
                    is Either.Left -> _signUpRequestState.value = RequestState.Error(it.value)
                    is Either.Right -> userRepository.signIn(
                        UserSignIn(
                            username = userSignUp.username,
                            password = userSignUp.password
                        )
                    ).collectRequest(_signInRequestState)
                }
            }
        }

    fun logout() = viewModelScope.launch { userRepository.logout() }

    /**
     * Collect network request and return [RequestState] depending on request result
     */
    private fun <T> Flow<Either<String, T>>.collectRequest(
        state: MutableStateFlow<RequestState<T>>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.value = RequestState.Loading()
            this@collectRequest.collect {
                when (it) {
                    is Either.Left -> state.value = RequestState.Error(it.value)
                    is Either.Right -> state.value = RequestState.Success(it.value)
                }
            }
        }
    }

    // Sign up data verification
    fun checkSignUpForm(
        username: String, email: String, password: String, passwordRepeat: String
    ): Either<Int, Int> {
        if (username.isBlank())
            return Either.Left(R.string.username_message)
        if (email.isBlank())
            return Either.Left(R.string.email_message)
        if (password.isBlank())
            return Either.Left(R.string.password_message)
        if (passwordRepeat.isBlank())
            return Either.Left(R.string.password_repeat_message)
        if (password != passwordRepeat)
            return Either.Left(R.string.passwords_do_not_match_message)
        else
            return Either.Right(R.string.success_message)
    }

    // Sign in data verification
    fun checkSignInForm(username: String, password: String): Either<Int, Int> {
        if (username.isBlank())
            return Either.Left(R.string.username_message)
        if (password.isBlank())
            return Either.Left(R.string.password_message)
        else
            return Either.Right(R.string.success_message)
    }
}

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(
    private val userRepository: UserRepository,
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(
                userRepository = userRepository,
                signInUseCase = signInUseCase,
                signUpUseCase = signUpUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}