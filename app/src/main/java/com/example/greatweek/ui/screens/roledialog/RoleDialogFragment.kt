package com.example.greatweek.ui.screens.roledialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.greatweek.R
import com.example.greatweek.App
import com.example.greatweek.ui.ViewModelFactory
import com.example.greatweek.databinding.RoleDialogLayoutBinding
import javax.inject.Inject

class RoleDialogFragment : DialogFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: RoleDialogFragmentViewModel by viewModels { viewModelFactory }

    private val role: String? by lazy { requireArguments().getString(ARG_ROLE) }
    private val requestKey: String by lazy { requireArguments().getString(ARG_REQUEST_KEY) ?: throw IllegalArgumentException() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = RoleDialogLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            roleEditText.setText(role)
            btnConfirm.setOnClickListener {
                val enteredText = binding.roleEditText.text.toString()
                if (enteredText.isBlank()) {
                    binding.roleEditText.error = getString(R.string.empty_value)
                    return@setOnClickListener
                }
                when (requestKey) {
                    RENAME_ROLE_REQUEST_KEY -> role?.let { viewModel.renameRole(oldName = it, newName = enteredText) }
                    ADD_ROLE_REQUEST_KEY -> viewModel.addRole(name = enteredText)
                }
                dismiss()
            }
            btnDismiss.setOnClickListener { dismiss() }
        }
        return dialog
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