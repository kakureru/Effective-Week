package com.example.schedule.presentation.role_pick_dialog

import com.example.schedule.domain.model.Role

class RoleItem(
    val name: String,
)

fun Role.toRoleItem() = RoleItem(
    name = name
)