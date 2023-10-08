package com.example.core.ui.theme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Green = Color(0XFF61BD50)
val Orange = Color(0xFFF9A33A)

val Neutral = Color(0xFF232323)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun greatTextFieldColors() = TextFieldDefaults.textFieldColors(
    containerColor = Color.Transparent,
)