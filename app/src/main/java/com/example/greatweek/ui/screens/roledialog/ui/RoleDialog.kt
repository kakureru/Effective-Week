package com.example.greatweek.ui.screens.roledialog.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.greatweek.ui.core.BasicDialog
import com.example.greatweek.ui.screens.roledialog.RoleDialogState
import com.example.greatweek.ui.screens.roledialog.RoleDialogViewModel
import com.example.greatweek.ui.theme.DarkTheme

@Composable
fun RoleDialog(
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
    viewModel: RoleDialogViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    RoleDialogUi(
        state = state,
        onDismissRequest = onDismissRequest,
        onConfirmClick = onConfirmClick,
        onNameChange = { text -> viewModel.onNameChanged(text) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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