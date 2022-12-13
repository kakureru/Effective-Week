package com.example.greatweek.presentation.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.greatweek.R
import com.example.greatweek.data.repository.GoalRepositoryImpl
import com.example.greatweek.data.repository.RoleRepositoryImpl
import com.example.greatweek.databinding.RoleDialogLayoutBinding
import com.example.greatweek.domain.usecase.role.AddRoleUseCase
import com.example.greatweek.domain.usecase.role.RenameRoleUseCase
import com.example.greatweek.presentation.Constants
import com.example.greatweek.presentation.GreatWeekApplication
import com.example.greatweek.presentation.viewmodel.RoleDialogFragmentViewModel
import com.example.greatweek.presentation.viewmodel.RoleDialogFragmentViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

typealias RoleDialogListener = (requestKey: String, roleName: String) -> Unit

class RoleDialogFragment : DialogFragment() {

    // repository

    private val roleRepository by lazy {
        RoleRepositoryImpl(
            roleDao = (activity?.application as GreatWeekApplication).database.RoleDao()
        )
    }

    // use cases
    private val addRoleUseCase by lazy { AddRoleUseCase(roleRepository) }
    private val renameRoleUseCase by lazy { RenameRoleUseCase(roleRepository) }

    // viewModel
    private val viewModel: RoleDialogFragmentViewModel by activityViewModels {
        RoleDialogFragmentViewModelFactory(
            addRoleUseCase = addRoleUseCase,
            renameRoleUseCase = renameRoleUseCase
        )
    }

    private fun addRole(name: String) {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.addRole(name = name)
        }
    }

    private fun renameRole(roleId: Int, newName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.renameRole(roleId = roleId, newName = newName)
        }
    }

    private val roleId: Int
        get() = requireArguments().getInt(ARG_ROLE_ID)

    private val roleName: String
        get() = requireArguments().getString(ARG_ROLE_NAME).toString()

    private val requestKey: String
        get() = requireArguments().getString(ARG_REQUEST_KEY)!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = RoleDialogLayoutBinding.inflate(layoutInflater)
        dialogBinding.roleEditText.setText(roleName)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.confirmButton.setOnClickListener {
            val enteredText = dialogBinding.roleEditText.text.toString()
            if (enteredText.isBlank()) {
                dialogBinding.roleEditText.error = getString(R.string.empty_value)
                return@setOnClickListener
            }
            when (requestKey) {
                Constants.KEY_RENAME_ROLE_REQUEST_KEY -> renameRole(roleId, enteredText)
                Constants.KEY_ADD_ROLE_REQUEST_KEY -> addRole(enteredText)
            }
            dismiss()
        }

        dialogBinding.dismissButton.setOnClickListener {
            dismiss()
        }

        dialog.setOnDismissListener { hideKeyboard(dialogBinding.roleEditText) }

        dialog.setOnShowListener {
            dialogBinding.roleEditText.requestFocus()
            showKeyboard(dialogBinding.roleEditText)
        }

        return dialog
    }

    private fun showKeyboard(view: View) {
        view.post {
            getInputMethodManager(view).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard(view: View) {
        getInputMethodManager(view).hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getInputMethodManager(view: View): InputMethodManager {
        val context = view.context
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    companion object {
        @JvmStatic
        private val TAG = RoleDialogFragment::class.java.simpleName

        @JvmStatic
        private val ARG_ROLE_NAME = "ARG_ROLE_NAME"

        @JvmStatic
        private val ARG_ROLE_ID = "ARG_ROLE_ID"

        @JvmStatic
        private val ARG_REQUEST_KEY = "ARG_REQUEST_KEY"

        fun show(
            manager: FragmentManager,
            roleId: Int = -1,
            roleName: String = "",
            requestKey: String
        ) {
            val dialogFragment = RoleDialogFragment()
            dialogFragment.arguments = bundleOf(
                ARG_ROLE_ID to roleId,
                ARG_ROLE_NAME to roleName,
                ARG_REQUEST_KEY to requestKey
            )
            dialogFragment.show(manager, TAG)
        }
    }
}