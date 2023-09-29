package com.example.greatweek.ui.screens.roledialog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.greatweek.R
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
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(value = state.name, onValueChange = { value -> onNameChange(value) })
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.action_cancel))
                    }
                    Button(onClick = {
                        if (state.name.isBlank()) {
//                            binding.roleEditText.error = getString(R.string.empty_value)
                        } else
                            onConfirmClick()
                    }) {
                        Text(text = stringResource(id = R.string.action_confirm))
                    }
                }
            }
        }
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