package com.example.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.ui.theme.DarkTheme

@Composable
fun AddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = "add")
    }
}

@Preview
@Composable
fun AddGoalButtonPreview() {
    DarkTheme {
        Surface {
            AddButton(onClick = {})
        }
    }
}