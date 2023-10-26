package com.effectiveweek.schedule.presentation.role_dialog

internal data class RoleDialogState(
    val name: String = "",
    val navState: RoleDialogNavState = RoleDialogNavState.Idle,
)

internal sealed class RoleDialogNavState {
    object Idle : RoleDialogNavState()
    object Dismiss : RoleDialogNavState()
}

internal sealed class RoleDialogEffect {
    class Error(val msgResource: Int) : RoleDialogEffect()
}