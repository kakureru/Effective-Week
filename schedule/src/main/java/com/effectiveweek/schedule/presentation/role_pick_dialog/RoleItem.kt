package com.effectiveweek.schedule.presentation.role_pick_dialog

import com.effectiveweek.schedule.domain.model.Role

internal class RoleItem(
    val name: String,
)

internal fun Role.toRoleItem() = RoleItem(
    name = name
)