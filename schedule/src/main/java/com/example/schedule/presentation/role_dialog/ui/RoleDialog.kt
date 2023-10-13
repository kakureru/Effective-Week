package com.example.schedule.presentation.role_dialog.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.ConfirmButton
import com.example.core.ui.theme.DarkTheme
import com.example.core.ui.theme.greatTextFieldColors
import com.example.schedule.R
import com.example.schedule.presentation.role_dialog.RoleDialogEffect
import com.example.schedule.presentation.role_dialog.RoleDialogNavState
import com.example.schedule.presentation.role_dialog.RoleDialogState
import com.example.schedule.presentation.role_dialog.RoleDialogViewModel

@Composable
fun RoleDialog(
    onDismissRequest: () -> Unit,
    viewModel: RoleDialogViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state.navState) {
        when (state.navState) {
            RoleDialogNavState.Dismiss -> { onDismissRequest() }
            RoleDialogNavState.Idle -> Unit
        }
    }
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is RoleDialogEffect.Error -> {
                    Toast.makeText(
                        context,
                        context.resources.getString(effect.msgResource),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    RoleDialogUi(
        state = state,
        onDismissRequest = onDismissRequest,
        onConfirmClick = {
            viewModel.onConfirmClick()
        },
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
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            TextField(
                value = state.name,
                onValueChange = { value -> onNameChange(value) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done,
                ),
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = stringResource(id = R.string.name))
                },
                colors = greatTextFieldColors(),
            )
            ConfirmButton(onClick = onConfirmClick, modifier = Modifier.align(Alignment.End))
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