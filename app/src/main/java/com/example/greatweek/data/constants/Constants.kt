package com.example.greatweek.data.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

// Retrofit
const val NEED_TOKEN_HEADER = "NEED_TOKEN_HEADER"

// Shared preferences
val AUTH_STATUS = booleanPreferencesKey("AUTH_STATUS")
val AUTH_TOKEN = stringPreferencesKey("AUTH_TOKEN")
val USERNAME = stringPreferencesKey("USERNAME")