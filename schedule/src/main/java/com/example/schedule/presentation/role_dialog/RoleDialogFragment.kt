package com.example.schedule.presentation.role_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.presentation.role_dialog.ui.RoleDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RoleDialogFragment : DialogFragment() {

    private val roleName: String? by lazy { arguments?.getString(ARG_ROLE_NAME) }

    private val viewModel: RoleDialogViewModel by viewModel { parametersOf(roleName) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DarkTheme {
                    RoleDialog(
                        viewModel = viewModel,
                        onDismissRequest = { dismiss() }
                    )
                }
            }
        }
    }

    companion object {
        private val TAG = RoleDialogFragment::class.java.simpleName
        private const val ARG_ROLE_NAME = "ARG_ROLE_NAME"

        fun show(manager: FragmentManager, roleName: String? = null) {
            RoleDialogFragment().apply {
                arguments = bundleOf(
                    ARG_ROLE_NAME to roleName,
                )
            }.show(manager, TAG)
        }
    }
}