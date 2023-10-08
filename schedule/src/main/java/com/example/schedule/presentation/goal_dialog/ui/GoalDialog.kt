package com.example.schedule.presentation.goal_dialog.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.ConfirmButton
import com.example.core.ui.GreatDatePicker
import com.example.core.ui.GreatTimePicker
import com.example.core.ui.theme.DarkTheme
import com.example.core.ui.theme.greatTextFieldColors
import com.example.schedule.R
import com.example.schedule.presentation.goal_dialog.GoalDialogEffect
import com.example.schedule.presentation.goal_dialog.GoalDialogEvent
import com.example.schedule.presentation.goal_dialog.GoalDialogNavState
import com.example.schedule.presentation.goal_dialog.GoalDialogNavigation
import com.example.schedule.presentation.goal_dialog.GoalDialogViewModel
import com.example.schedule.presentation.role_pick_dialog.RoleItem
import com.example.schedule.presentation.role_pick_dialog.ui.RolePickDialog
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun GoalDialog(
    navigation: GoalDialogNavigation,
    viewModel: GoalDialogViewModel,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state.navState) {
        when (state.navState) {
            GoalDialogNavState.Idle -> Unit
            GoalDialogNavState.Dismiss -> navigation.dismiss()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect {
            when (it) {
                is GoalDialogEffect.Error -> scope.launch {
                    Toast.makeText(
                        context,
                        context.resources.getString(it.msgResource),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
        role = {
            RolePickField(
                role = state.role,
                availableRoles = state.availableRoles,
                onRolePicked = { roleName -> viewModel.accept(GoalDialogEvent.RolePick(roleName)) },
                onAddRoleClick = { }
            )
        },
        date = {
            DateField(
                date = state.date,
                onDateSelected = { dateMillis ->
                    viewModel.accept(GoalDialogEvent.DatePick(dateMillis))
                }
            )
        },
        time = {
            TimeField(
                time = state.timePrint,
                onTimeSelected = { h, m -> viewModel.accept(GoalDialogEvent.TimePick(h, m)) },
                initialTime = state.time
            )
        },
        appointment = {
            AppointmentField(
                isAppointment = state.appointment,
                onValueChange = {
                    viewModel.accept(GoalDialogEvent.IsAppointmentClick)
                }
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDialogUi(
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    role: @Composable () -> Unit,
    date: @Composable () -> Unit,
    time: @Composable () -> Unit,
    appointment: @Composable () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        dragHandle = null,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 32.dp)
        ) {
            title()
            description()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                role()
                date()
                time()
            }
            appointment()
            ConfirmButton(onClick = onConfirmClick, modifier = Modifier.align(Alignment.End))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
        ),
        colors = greatTextFieldColors()
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
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(id = R.string.description)
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
        colors = greatTextFieldColors()
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
    Box {
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .clickable { dialogVisible = true }
                .padding(vertical = 8.dp)
        ) {
            Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
            Text(text = role ?: stringResource(id = R.string.role))
        }
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
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable { datePickDialogVisible = true }
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
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable { timePickerDialogVisible = true }
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
    onValueChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.clickable { onValueChange() }
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(checked = isAppointment, onCheckedChange = { onValueChange() })
        }
        Text(text = stringResource(id = R.string.appointment))
    }
}

@Preview
@Composable
private fun GoalDialogPreview() {
    DarkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GoalDialogUi(
                onDismissRequest = {},
                onConfirmClick = {},
                title = { TitleField(value = "", onValueChange = {}) },
                description = { DescriptionField(value = "", onValueChange = {}) },
                role = {
                    RolePickField(
                        role = null,
                        availableRoles = emptyList(),
                        onAddRoleClick = {},
                        onRolePicked = {})
                },
                date = {
                },
                time = {},
                appointment = { AppointmentField(isAppointment = false, onValueChange = {}) },
            )
        }
    }
}