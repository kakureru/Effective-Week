package com.example.greatweek.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.greatweek.data.constants.*
import com.example.greatweek.data.model.network.fromDomain
import com.example.greatweek.data.model.network.toDomain
import com.example.greatweek.data.network.GreatWeekApi
import com.example.greatweek.domain.model.network.UserSignIn
import com.example.greatweek.domain.model.network.UserSignUp
import com.example.greatweek.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val greatWeekApi: GreatWeekApi,
    private val prefDataStore: DataStore<Preferences>
    ) : BaseRepository(), UserRepository {

    override var isAuthorised: Flow<Boolean> = prefDataStore.data.map {
        it[AUTH_STATUS] ?: false
    }

    override val username: Flow<String> = prefDataStore.data.map {
        it[USERNAME] ?: ""
    }

    override fun signUp(userSignUp: UserSignUp) = doRequest {
        greatWeekApi.signUp(userSignUp.fromDomain()).also {
            Log.d("TAG", "Sign up response -> $it")
        }.toDomain()
    }

    override fun signIn(userSignIn: UserSignIn) = doRequest {
        greatWeekApi.signIn(userSignIn.fromDomain()).also {
            Log.d("TAG", "Sign in response -> $it")
            prefDataStore.edit { pref ->
                pref[AUTH_STATUS] = true
                pref[AUTH_TOKEN] = it.token
                pref[USERNAME] = userSignIn.username
            }
        }.toDomain()
    }

    override suspend fun logout() {
        prefDataStore.edit { pref ->
            pref[AUTH_STATUS] = false
            pref.remove(AUTH_TOKEN)
            pref.remove(USERNAME)
        }
    }
}