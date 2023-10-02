package com.example.schedule.presentation.role_pick_dialog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.R
import com.example.schedule.presentation.role_pick_dialog.RoleItem
import com.example.schedule.presentation.role_pick_dialog.RolePickerViewModel

@Composable
fun RolePickDialog(
    viewModel: RolePickerViewModel,
    onRolePicked: (roleName: String) -> Unit,
    onDismissRequest: () -> Unit,
    onAddRoleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val roles by viewModel.roles.collectAsState()
    RolePickDialogUi(
        roles = roles,
        onRolePicked = onRolePicked,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        onAddRoleClick = onAddRoleClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolePickDialogUi(
    roles: List<RoleItem>,
    onRolePicked: (roleName: String) -> Unit,
    onDismissRequest: () -> Unit,
    onAddRoleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        LazyColumn {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = stringResource(id = R.string.choose_role))
                    IconButton(onClick = onAddRoleClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_role_add),
                            contentDescription = "add role"
                        )
                    }
                }
            }
            items(items = roles, key = { item -> item.name }) {
                RolePickItem(name = it.name, onClick = { onRolePicked(it.name) })
            }
        }
    }
}

@Composable
fun RolePickItem(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onClick() }
    ) {
        Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
        Text(text = name)
    }
}

@Preview
@Composable
fun RolePickDialogPreview() {
    DarkTheme {
        RolePickDialogUi(
            roles = rolesPreviewList,
            onDismissRequest = {},
            onRolePicked = {},
            onAddRoleClick = {}
        )
    }
}

private val rolesPreviewList = listOf(
    RoleItem("First"),
    RoleItem("Second"),
    RoleItem("Third"),
)