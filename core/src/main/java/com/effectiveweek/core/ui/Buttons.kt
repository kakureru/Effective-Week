package com.effectiveweek.core.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.effectiveweek.core.ui.theme.DarkTheme

@Composable
fun AddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = "add", modifier = Modifier.size(28.dp))
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

@Composable
fun ConfirmButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            imageVector = Icons.Rounded.Done,
            contentDescription = "done",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun ConfirmButtonPreview() {
    DarkTheme {
        Surface {
            ConfirmButton(onClick = {})
        }
    }
}