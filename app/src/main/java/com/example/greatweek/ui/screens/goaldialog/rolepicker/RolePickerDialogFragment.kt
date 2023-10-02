package com.example.greatweek.ui.screens.goaldialog.rolepicker

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
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.example.greatweek.App
import com.example.greatweek.ui.ViewModelFactory
import com.example.greatweek.ui.screens.goaldialog.rolepicker.ui.RolePickDialog
import com.example.greatweek.ui.screens.roledialog.RoleDialogFragment
import com.example.greatweek.ui.theme.DarkTheme
import javax.inject.Inject

class RolePickerDialogFragment : DialogFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: RolePickerViewModel by viewModels { viewModelFactory }

    private val onRoleItemClick = { role: String ->
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(KEY_ROLE_RESPONSE to role)
        )
        dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as App).appComponent.inject(this)
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
        RoleDialogFragment.showForNew(manager = parentFragmentManager)
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