package com.example.schedule.presentation.role_pick_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.presentation.role_dialog.RoleDialogFragment
import com.example.schedule.presentation.role_pick_dialog.ui.RolePickDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class RolePickerDialogFragment : DialogFragment() {

    private val viewModel: RolePickerViewModel by viewModel()

    private val onRoleItemClick = { role: String ->
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(KEY_ROLE_RESPONSE to role)
        )
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply { 
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DarkTheme {
                    RolePickDialog(
                        viewModel = viewModel,
                        onRolePicked = onRoleItemClick,
                        onDismissRequest = { dismiss() },
                        onAddRoleClick = { showNewRoleDialog() }
                    )
                }
            }
        }
    }

    private fun showNewRoleDialog() {
        RoleDialogFragment.show(manager = parentFragmentManager)
    }

    companion object {
        private val TAG = RolePickerDialogFragment::class.java.simpleName
        private const val REQUEST_KEY = "REQUEST_KEY"
        private const val KEY_ROLE_RESPONSE = "KEY_ROLE_RESPONSE"

        fun show(manager: FragmentManager) = RolePickerDialogFragment().show(manager, TAG)

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (role: String) -> Unit
        ) {
            manager.setFragmentResultListener(
                REQUEST_KEY,
                lifecycleOwner,
                FragmentResultListener { _, result ->
                    listener(result.getString(KEY_ROLE_RESPONSE)!!)
                }
            )
        }
    }
}