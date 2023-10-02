package com.example.schedule.presentation.role_dialog

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.core.ui.theme.DarkTheme
import com.example.schedule.di.ScheduleComponentViewModel
import com.example.schedule.presentation.role_dialog.ui.RoleDialog
import javax.inject.Inject

class RoleDialogFragment : DialogFragment() {

    private val roleName: String? by lazy { arguments?.getString(ARG_ROLE_NAME) }

    @Inject lateinit var viewModelFactory: RoleDialogViewModelFactory.Factory
    private val viewModel: RoleDialogViewModel by viewModels { viewModelFactory.create(roleName) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<ScheduleComponentViewModel>().newScheduleComponent.inject(this)
    }

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