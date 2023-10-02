package com.example.greatweek.ui.screens.goaldialog.rolepicker

import com.example.greatweek.domain.model.Role

class RoleItem(
    val name: String,
)

fun Role.toRoleItem() = RoleItem(
    name = name
)