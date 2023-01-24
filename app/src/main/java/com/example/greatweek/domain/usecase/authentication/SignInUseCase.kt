package com.example.greatweek.domain.usecase.authentication

import com.example.greatweek.domain.model.network.UserSignIn
import com.example.greatweek.domain.repository.UserRepository

class SignInUseCase(private val userRepository: UserRepository) {
    operator fun invoke(userSignIn: UserSignIn) = userRepository.signIn(userSignIn)
}