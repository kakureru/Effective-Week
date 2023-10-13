package com.example.schedule.presentation.role_dialog

data class RoleDialogState(
    val name: String = "",
    val navState: RoleDialogNavState = RoleDialogNavState.Idle,
)

sealed class RoleDialogNavState {
    object Idle : RoleDialogNavState()
    object Dismiss : RoleDialogNavState()
}

sealed class RoleDialogEffect {
    class Error(val msgResource: Int) : RoleDialogEffect()
}