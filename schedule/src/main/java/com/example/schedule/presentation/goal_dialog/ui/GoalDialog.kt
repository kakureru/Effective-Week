package com.example.schedule.presentation.goal_dialog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.BasicDialog
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.R
import com.example.schedule.presentation.goal_dialog.GoalDialogEvent
import com.example.schedule.presentation.goal_dialog.GoalDialogViewModel

@Composable
fun GoalDialog(
    viewModel: GoalDialogViewModel,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    GoalDialogUi(
        isSuccess = state.isSuccessful,
        onConfirmClick = {
            viewModel.accept(GoalDialogEvent.ConfirmClick)
            onDismissRequest()
        },
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        title = {
            TitleField(
                value = state.title,
                onValueChange = { value -> viewModel.accept(GoalDialogEvent.TitleChanged(value)) }
            )
        },
        description = {
            DescriptionField(
                value = state.description,
                onValueChange = { value -> viewModel.accept(GoalDialogEvent.DescriptionChanged(value)) }
            )
        },
        roleField = {
            RoleField(
                role = state.role,
                onClick = { viewModel.accept(GoalDialogEvent.RoleClick) }
            )
        },
        dateTime = {
            DateTimeFields(
                date = state.date,
                time = state.time,
                onDateClick = { viewModel.accept(GoalDialogEvent.DateClick) },
                onTimeClick = { viewModel.accept(GoalDialogEvent.TimeClick) },
            )
        },
        appointment = {
            AppointmentField(
                isAppointment = state.appointment,
                onValueChange = { value ->
                    viewModel.accept(
                        GoalDialogEvent.AppointmentValueChanged(value)
                    )
                }
            )
        },
    )
}

@Composable
fun GoalDialogUi(
    isSuccess: Boolean,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    roleField: @Composable () -> Unit,
    dateTime: @Composable () -> Unit,
    appointment: @Composable () -> Unit,
) {
    LaunchedEffect(key1 = isSuccess) {
        if (isSuccess) onDismissRequest()
    }
    BasicDialog(
        onConfirmClick = onConfirmClick,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        title()
        description()
        roleField()
        dateTime()
        appointment()
    }
}

@Composable
private fun TitleField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text(
                text = stringResource(id = R.string.goal)
            )
        },
        modifier = modifier,
    )
}

@Composable
private fun DescriptionField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = modifier,
        placeholder = {
            Text(
                text = stringResource(id = R.string.description)
            )
        }
    )
}


@Composable
private fun RoleField(
    role: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onClick() }
    ) {
        Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
        Text(text = role ?: stringResource(id = R.string.role))
    }
}

@Composable
private fun DateTimeFields(
    date: String?,
    time: String?,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        DateField(date = date, onClick = onDateClick)
        Spacer(modifier = Modifier.width(16.dp))
        TimeField(time = time, onClick = onTimeClick)
    }
}

@Composable
private fun DateField(
    date: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onClick() }
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_calendar), contentDescription = null)
        Text(text = date ?: stringResource(id = R.string.date))
    }
}

@Composable
private fun TimeField(
    time: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onClick() }
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_time), contentDescription = null)
        Text(text = time ?: stringResource(id = R.string.time))
    }
}

@Composable
private fun AppointmentField(
    isAppointment: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Checkbox(checked = isAppointment, onCheckedChange = { onValueChange(it) })
        Text(text = stringResource(id = R.string.appointment))
    }
}

@Preview
@Composable
private fun GoalDialogPreview() {
    DarkTheme {
        GoalDialogUi(
            isSuccess = false,
            onDismissRequest = {},
            onConfirmClick = {},
            title = { TitleField(value = "", onValueChange = {}) },
            description = { DescriptionField(value = "", onValueChange = {}) },
            roleField = { RoleField(role = null, onClick = {}) },
            dateTime = {
                DateTimeFields(
                    date = null,
                    time = null,
                    onDateClick = {},
                    onTimeClick = {})
            },
            appointment = { AppointmentField(isAppointment = false, onValueChange = {}) },
        )
    }
}