package com.effectiveweek.schedule.presentation.role_dialog.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.effectiveweek.core.ui.ConfirmButton
import com.effectiveweek.core.ui.theme.DarkTheme
import com.effectiveweek.core.ui.theme.greatTextFieldColors
import com.effectiveweek.schedule.R
import com.effectiveweek.schedule.presentation.role_dialog.RoleDialogEffect
import com.effectiveweek.schedule.presentation.role_dialog.RoleDialogNavState
import com.effectiveweek.schedule.presentation.role_dialog.RoleDialogState
import com.effectiveweek.schedule.presentation.role_dialog.RoleDialogViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            RoleDialogNavState.Dismiss -> {
                onDismissRequest()
            }

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
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        dragHandle = null,
        shape = MaterialTheme.shapes.large.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        )
    ) {
        RoleDialogUi(
            state = state,
            onConfirmClick = {
                viewModel.onConfirmClick()
            },
            onNameChange = { text -> viewModel.onNameChanged(text) },
            modifier = modifier,
        )
    }
}

@Composable
fun RoleDialogUi(
    state: RoleDialogState,
    onNameChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Row(
        modifier = modifier.padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            textStyle = MaterialTheme.typography.titleLarge,
            value = state.name,
            onValueChange = { value -> onNameChange(value) },
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.name),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            colors = greatTextFieldColors(),
        )
        ConfirmButton(onClick = onConfirmClick)
    }

}

@Preview
@Composable
fun RoleDialogPreview() {
    DarkTheme {
        Surface(
            shape = MaterialTheme.shapes.large.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            )
        ) {
            RoleDialogUi(
                onNameChange = {},
                onConfirmClick = {},
                state = RoleDialogState(),
            )
        }
    }
}