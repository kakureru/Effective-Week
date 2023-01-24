package com.example.greatweek.domain.usecase.authentication

import com.example.greatweek.domain.model.network.UserSignUp
import com.example.greatweek.domain.repository.UserRepository

class SignUpUseCase(private val userRepository: UserRepository) {
    operator fun invoke(userSignUp: UserSignUp) = userRepository.signUp(userSignUp)
}