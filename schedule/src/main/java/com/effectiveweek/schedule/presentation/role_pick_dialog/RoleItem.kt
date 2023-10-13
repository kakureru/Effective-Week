package com.effectiveweek.schedule.presentation.role_pick_dialog

import com.effectiveweek.schedule.domain.model.Role

class RoleItem(
    val name: String,
)

fun Role.toRoleItem() = RoleItem(
    name = name
)