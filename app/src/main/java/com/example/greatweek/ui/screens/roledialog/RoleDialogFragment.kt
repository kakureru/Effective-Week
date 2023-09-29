package com.example.greatweek.ui.screens.roledialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.greatweek.App
import com.example.greatweek.ui.ViewModelFactory
import com.example.greatweek.ui.screens.roledialog.ui.RoleDialog
import com.example.greatweek.ui.theme.DarkTheme
import javax.inject.Inject

class RoleDialogFragment : DialogFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: RoleDialogViewModel by viewModels { viewModelFactory }

    private val role: String? by lazy { requireArguments().getString(ARG_ROLE) }
    private val requestKey: String by lazy { requireArguments().getString(ARG_REQUEST_KEY) ?: throw IllegalArgumentException() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DarkTheme {
                    RoleDialog(
                        viewModel = viewModel,
                        onConfirmClick = {
                            when (requestKey) {
                                RENAME_ROLE_REQUEST_KEY -> role?.let { viewModel.renameRole(oldName = it) }
                                ADD_ROLE_REQUEST_KEY -> viewModel.addRole()
                            }
                            dismiss()
                        },
                        onDismissRequest = {
                            dismiss()
                        }
                    )
                }
            }
        }
    }

    companion object {
        private val TAG = RoleDialogFragment::class.java.simpleName
        private const val ARG_ROLE = "ARG_ROLE_NAME"
        private const val ARG_REQUEST_KEY = "ARG_REQUEST_KEY"

        const val ADD_ROLE_REQUEST_KEY = "ADD_ROLE_REQUEST_KEY"
        const val RENAME_ROLE_REQUEST_KEY = "RENAME_ROLE_REQUEST_KEY"

        fun showForNew(manager: FragmentManager) {
            RoleDialogFragment().apply {
                arguments = bundleOf(
                    ARG_REQUEST_KEY to ADD_ROLE_REQUEST_KEY
                )
            }.show(manager, TAG)
        }

        fun showForEdit(manager: FragmentManager, role: String) {
            RoleDialogFragment().apply {
                arguments = bundleOf(
                    ARG_ROLE to role,
                    ARG_REQUEST_KEY to RENAME_ROLE_REQUEST_KEY
                )
            }.show(manager, TAG)
        }
    }
}