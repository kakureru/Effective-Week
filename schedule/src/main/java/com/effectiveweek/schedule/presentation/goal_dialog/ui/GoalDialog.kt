package com.effectiveweek.schedule.presentation.goal_dialog.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.effectiveweek.core.ui.ConfirmButton
import com.effectiveweek.core.ui.GreatDatePicker
import com.effectiveweek.core.ui.GreatTimePicker
import com.effectiveweek.core.ui.theme.DarkTheme
import com.effectiveweek.core.ui.theme.greatTextFieldColors
import com.effectiveweek.schedule.R
import com.effectiveweek.schedule.presentation.goal_dialog.GoalDialogEffect
import com.effectiveweek.schedule.presentation.goal_dialog.GoalDialogEvent
import com.effectiveweek.schedule.presentation.goal_dialog.GoalDialogNavState
import com.effectiveweek.schedule.presentation.goal_dialog.GoalDialogNavigation
import com.effectiveweek.schedule.presentation.goal_dialog.GoalDialogViewModel
import com.effectiveweek.schedule.presentation.role_pick_dialog.RoleItem
import com.effectiveweek.schedule.presentation.role_pick_dialog.ui.RolePickDialog
import kotlinx.coroutines.launch
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
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
    ModalBottomSheet(
        onDismissRequest = { navigation.dismiss() },
        dragHandle = null,
        shape = MaterialTheme.shapes.large.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        )
    ) {
        GoalDialogUi(
            onConfirmClick = { viewModel.accept(GoalDialogEvent.ConfirmClick) },
            modifier = modifier,
            isAddingDescription = state.isAddingDescription,
            title = {
                TitleField(
                    value = state.title,
                    onValueChange = { value -> viewModel.accept(GoalDialogEvent.TitleChanged(value)) },
                )
            },
            description = {
                DescriptionField(
                    value = state.description,
                    onValueChange = { value ->
                        viewModel.accept(GoalDialogEvent.DescriptionChanged(value))
                    }
                )
            },
            pickers = {
                item {
                    RolePicker(
                        role = state.role,
                        availableRoles = state.availableRoles,
                        onRolePicked = { roleName ->
                            viewModel.accept(GoalDialogEvent.RolePick(roleName))
                        },
                        onAddRoleClick = { navigation.openRoleDialog() }
                    )
                }
                if (state.isAddingDescription.not()) {
                    item {
                        DescriptionPicker(onClick = { viewModel.accept(GoalDialogEvent.DescriptionPickerClick) })
                    }
                }
                item {
                    DatePicker(
                        date = state.date,
                        onDateSelected = { dateMillis ->
                            viewModel.accept(GoalDialogEvent.DatePick(dateMillis))
                        }
                    )
                }
                item {
                    TimePicker(
                        time = state.timePrint,
                        onTimeSelected = { h, m ->
                            viewModel.accept(GoalDialogEvent.TimePick(h, m))
                        },
                        initialTime = state.time
                    )
                }
                item {
                    AppointmentPicker(
                        isAppointment = state.appointment,
                        onValueChange = {
                            viewModel.accept(GoalDialogEvent.IsAppointmentClick)
                        }
                    )
                }
            },
        )
    }
}

@Composable
fun GoalDialogUi(
    onConfirmClick: () -> Unit,
    isAddingDescription: Boolean,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    pickers: LazyListScope.() -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            pickers()
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                title()
                if (isAddingDescription) {
                    description()
                }
            }
            ConfirmButton(onClick = onConfirmClick)
        }
    }
}

@Composable
private fun TitleField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    TextField(
        value = value,
        textStyle = MaterialTheme.typography.titleLarge,
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text(
                text = stringResource(id = R.string.goal),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
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
        modifier = modifier
            .fillMaxWidth(),
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
fun DescriptionPicker(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_short_text),
            contentDescription = "add description"
        )
    }
}

@Composable
private fun RolePicker(
    role: String?,
    availableRoles: List<RoleItem>,
    onRolePicked: (roleName: String) -> Unit,
    onAddRoleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var dialogVisible by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
    ) {
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
            modifier = Modifier.clickable { dialogVisible = true }
        ) {
            IconButton(onClick = { dialogVisible = true }) {
                Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
            }
            role?.let {
                Text(text = role, modifier = Modifier.padding(end = 8.dp))
            }
        }
    }
}

@Composable
private fun DatePicker(
    date: String?,
    onDateSelected: (dateMillis: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var datePickDialogVisible by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { datePickDialogVisible = true }
    ) {
        IconButton(onClick = { datePickDialogVisible = true }) {
            Icon(painter = painterResource(id = R.drawable.ic_calendar), contentDescription = null)
        }
        date?.let {
            Text(text = date, modifier = Modifier.padding(end = 8.dp))
        }
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
private fun TimePicker(
    time: String?,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier,
    initialTime: LocalTime,
) {
    var timePickerDialogVisible by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { timePickerDialogVisible = true }
    ) {
        IconButton(onClick = { timePickerDialogVisible = true }) {
            Icon(painter = painterResource(id = R.drawable.ic_time), contentDescription = null)
        }
        time?.let {
            Text(text = time, modifier = Modifier.padding(end = 8.dp))
        }
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

@Composable
private fun AppointmentPicker(
    isAppointment: Boolean,
    onValueChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onValueChange() }
    ) {
        Checkbox(checked = isAppointment, onCheckedChange = { onValueChange() })
        Text(
            text = stringResource(id = R.string.appointment),
            modifier = Modifier.padding(end = 12.dp)
        )
    }
}

@Preview
@Composable
private fun GoalDialogPreviewNewGoal() {
    DarkTheme {
        Surface(
            shape = MaterialTheme.shapes.large.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            )
        ) {
            GoalDialogUi(
                onConfirmClick = {},
                title = {
                    TitleField(
                        value = "",
                        onValueChange = {},
                    )
                },
                description = { DescriptionField(value = "", onValueChange = {}) },
                isAddingDescription = false,
                pickers = {
                    item {
                        RolePicker(
                            role = null,
                            availableRoles = emptyList(),
                            onAddRoleClick = {},
                            onRolePicked = {}
                        )
                    }
                    item {
                        DescriptionPicker(onClick = {})
                    }
                    item {
                        DatePicker(
                            date = null,
                            onDateSelected = { }
                        )
                    }
                    item {
                        TimePicker(
                            time = null,
                            onTimeSelected = { h, m -> },
                            initialTime = LocalTime.MIN
                        )
                    }
                    item {
                        AppointmentPicker(isAppointment = false, onValueChange = {})
                    }
                },
            )
        }
    }
}

@Preview
@Composable
private fun GoalDialogPreview() {
    DarkTheme {
        Surface(
            shape = MaterialTheme.shapes.large.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            )
        ) {
            GoalDialogUi(
                onConfirmClick = {},
                title = {
                    TitleField(
                        value = "Sample goal",
                        onValueChange = {},
                    )
                },
                description = { DescriptionField(value = "", onValueChange = {}) },
                isAddingDescription = true,
                pickers = {
                    item {
                        RolePicker(
                            role = "Sample role",
                            availableRoles = emptyList(),
                            onAddRoleClick = {},
                            onRolePicked = {}
                        )
                    }
                    item {
                        DatePicker(
                            date = "Oct 20",
                            onDateSelected = { }
                        )
                    }
                    item {
                        TimePicker(
                            time = "12:00",
                            onTimeSelected = { h, m -> },
                            initialTime = LocalTime.MIN
                        )
                    }
                    item {
                        AppointmentPicker(isAppointment = false, onValueChange = {})
                    }
                },
            )
        }
    }
}