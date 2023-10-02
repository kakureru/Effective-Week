package com.example.schedule.presentation.role_dialog.ui

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.ui.BasicDialog
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.presentation.role_dialog.RoleDialogState
import com.example.schedule.presentation.role_dialog.RoleDialogViewModel

@Composable
fun RoleDialog(
    onDismissRequest: () -> Unit,
    viewModel: RoleDialogViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    RoleDialogUi(
        state = state,
        onDismissRequest = onDismissRequest,
        onConfirmClick = {
            viewModel.onConfirmClick()
            onDismissRequest()
        },
        onNameChange = { text -> viewModel.onNameChanged(text) },
        modifier = modifier,
    )
}

@Composable
fun RoleDialogUi(
    state: RoleDialogState,
    onNameChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicDialog(
        onConfirmClick = {
            if (state.name.isBlank()) {
//                            binding.roleEditText.error = getString(R.string.empty_value)
            } else
                onConfirmClick()
        },
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        TextField(value = state.name, onValueChange = { value -> onNameChange(value) })
    }
}

@Preview
@Composable
fun RoleDialogPreview() {
    DarkTheme {
        RoleDialogUi(
            onDismissRequest = {},
            onNameChange = {},
            onConfirmClick = {},
            state = RoleDialogState(),
        )
    }
}