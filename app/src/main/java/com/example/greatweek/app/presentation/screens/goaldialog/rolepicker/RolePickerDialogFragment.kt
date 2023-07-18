package com.example.greatweek.app.presentation.screens.goaldialog.rolepicker

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.example.greatweek.app.App
import com.example.greatweek.app.presentation.ViewModelFactory
import com.example.greatweek.databinding.RoleBottomSheetDialogLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        val binding = RoleBottomSheetDialogLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        val roleAdapter = RoleBottomSheetDialogAdapter(onItemClick = onRoleItemClick)
        binding.roleRecyclerView.adapter = roleAdapter
        viewModel.roles.observe(this) {
            roleAdapter.submitList(it)
        }
        return dialog
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