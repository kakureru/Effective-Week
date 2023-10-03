package com.example.schedule.presentation.goal_dialog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.BasicDialog
import com.example.core.ui.GreatDatePicker
import com.example.core.ui.GreatTimePicker
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.R
import com.example.schedule.presentation.goal_dialog.GoalDialogEvent
import com.example.schedule.presentation.goal_dialog.GoalDialogNavState
import com.example.schedule.presentation.goal_dialog.GoalDialogNavigation
import com.example.schedule.presentation.goal_dialog.GoalDialogViewModel
import com.example.schedule.presentation.role_pick_dialog.RoleItem
import com.example.schedule.presentation.role_pick_dialog.ui.RolePickDialog
import java.time.LocalTime

@Composable
fun GoalDialog(
    navigation: GoalDialogNavigation,
    viewModel: GoalDialogViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state.navState) {
        when (state.navState) {
            GoalDialogNavState.Idle -> Unit
            GoalDialogNavState.Dismiss -> navigation.dismiss()
        }
    }
    GoalDialogUi(
        onConfirmClick = { viewModel.accept(GoalDialogEvent.ConfirmClick) },
        onDismissRequest = { navigation.dismiss() },
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
            RolePickField(
                role = state.role,
                availableRoles = state.availableRoles,
                onRolePicked = { roleName -> viewModel.accept(GoalDialogEvent.RolePick(roleName)) },
                onAddRoleClick = { }
            )
        },
        dateTime = {
            DateTimeFields(
                date = state.date,
                time = state.timePrint,
                onDateSelected = { dateMillis -> viewModel.accept(GoalDialogEvent.DatePick(dateMillis)) },
                onTimeSelected = { hour, minute ->
                    viewModel.accept(
                        GoalDialogEvent.TimePick(
                            hour,
                            minute
                        )
                    )
                },
                initialTime = state.time,
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
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    roleField: @Composable () -> Unit,
    dateTime: @Composable () -> Unit,
    appointment: @Composable () -> Unit,
) {
    BasicDialog(
        onConfirmClick = onConfirmClick,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            title()
            description()
            roleField()
            dateTime()
            appointment()
        }
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
private fun RolePickField(
    role: String?,
    availableRoles: List<RoleItem>,
    onRolePicked: (roleName: String) -> Unit,
    onAddRoleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var dialogVisible by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.clickable { dialogVisible = true }
    ) {
        Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
        Text(text = role ?: stringResource(id = R.string.role))
        if (dialogVisible) {
            RolePickDialog(
                roles = availableRoles,
                onRolePicked = { picked ->
                    onRolePicked(picked)
                    dialogVisible = false
                },
                onAddRoleClick = onAddRoleClick,
                onDismissRequest = { dialogVisible = false }
            )
        }
    }
}

@Composable
private fun DateTimeFields(
    date: String?,
    time: String?,
    initialTime: LocalTime,
    onDateSelected: (dateMillis: Long) -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        DateField(date = date, onDateSelected = onDateSelected)
        Spacer(modifier = Modifier.width(16.dp))
        TimeField(time = time, onTimeSelected = onTimeSelected, initialTime = initialTime)
    }
}

@Composable
private fun DateField(
    date: String?,
    onDateSelected: (dateMillis: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var datePickDialogVisible by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.clickable { datePickDialogVisible = true }
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_calendar), contentDescription = null)
        Text(text = date ?: stringResource(id = R.string.date))
    }
    if (datePickDialogVisible) {
        GreatDatePicker(
            onDismissRequest = { datePickDialogVisible = false },
            onDatePick = {
                onDateSelected(it)
                datePickDialogVisible = false
            }
        )
    }
}

@Composable
private fun TimeField(
    time: String?,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier,
    initialTime: LocalTime,
) {
    var timePickerDialogVisible by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.clickable { timePickerDialogVisible = true }
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_time), contentDescription = null)
        Text(text = time ?: stringResource(id = R.string.time))
    }
    if (timePickerDialogVisible) {
        GreatTimePicker(
            onTimeSelected = { h, m ->
                onTimeSelected(h, m)
                timePickerDialogVisible = false
            },
            onDismissRequest = { timePickerDialogVisible = false },
            initialHour = initialTime.hour,
            initialMinute = initialTime.minute,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppointmentField(
    isAppointment: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(checked = isAppointment, onCheckedChange = { onValueChange(it) })
        }
        Text(text = stringResource(id = R.string.appointment))
    }
}

@Preview
@Composable
private fun GoalDialogPreview() {
    DarkTheme {
        GoalDialogUi(
            onDismissRequest = {},
            onConfirmClick = {},
            title = { TitleField(value = "", onValueChange = {}) },
            description = { DescriptionField(value = "", onValueChange = {}) },
            roleField = {
                RolePickField(
                    role = null,
                    availableRoles = emptyList(),
                    onAddRoleClick = {},
                    onRolePicked = {})
            },
            dateTime = {
                DateTimeFields(
                    date = null,
                    time = null,
                    onDateSelected = {},
                    onTimeSelected = { h, m -> },
                    initialTime = LocalTime.MIDNIGHT,
                )
            },
            appointment = { AppointmentField(isAppointment = false, onValueChange = {}) },
        )
    }
}