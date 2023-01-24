package com.example.greatweek.domain.repository

import com.example.greatweek.domain.model.network.SignIn
import com.example.greatweek.domain.model.network.SignUp
import com.example.greatweek.domain.model.network.UserSignIn
import com.example.greatweek.domain.model.network.UserSignUp
import com.example.greatweek.domain.utils.Either
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    var isAuthorised: Flow<Boolean>

    val username: Flow<String>

    fun signUp(userSignUp: UserSignUp): Flow<Either<String, SignUp>>

    fun signIn(userSignIn: UserSignIn): Flow<Either<String, SignIn>>

    suspend fun logout()
}