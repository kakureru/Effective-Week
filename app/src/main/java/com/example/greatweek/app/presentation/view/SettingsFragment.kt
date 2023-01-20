package com.example.greatweek.app.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.greatweek.R
import com.example.greatweek.app.presentation.viewmodel.SettingsViewModel
import com.example.greatweek.databinding.FragmentRootSettingsBinding
import com.example.greatweek.databinding.SignInWindowBinding
import com.example.greatweek.databinding.SignUpWindowBinding

class SettingsFragment : Fragment() {

    private val viewModel by activityViewModels<SettingsViewModel>()

    private var _binding: FragmentRootSettingsBinding? = null
    val binding get() = _binding!!

    @Suppress("RedundantNullableReturnType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRootSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsFragment = this@SettingsFragment
    }

    /**
     * Sign up dialog
     */

    fun showSignUpDialog() {
        val dialogBinding = SignUpWindowBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.registration))
            .setMessage(resources.getString(R.string.registration_message))
            .setView(dialogBinding.root)
            .setNegativeButton(resources.getString(R.string.action_cancel)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.action_confirm), null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val username = dialogBinding.usernameEditText.text.toString()
                val email = dialogBinding.emailEditText.text.toString()
                val password = dialogBinding.passwordEditText.text.toString()
                val passwordRepeat = dialogBinding.repeatPasswordEditText.text.toString()
                val (checkResult, checkMessage) = checkRegistrationForm(
                    username, email, password, passwordRepeat
                )
                if (!checkResult) {
                    Toast.makeText(context, checkMessage, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.register(username, password, email)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    /**
     * Reg data verification
     */

    private fun checkRegistrationForm(
        username: String, email: String, password: String, passwordRepeat: String
    ): Pair<Boolean, String> {
        if (username.isBlank())
            return Pair(false, resources.getString(R.string.username_message))
        if (email.isBlank())
            return Pair(false, resources.getString(R.string.email_message))
        if (password.isBlank())
            return Pair(false, resources.getString(R.string.password_message))
        if (passwordRepeat.isBlank())
            return Pair(false, resources.getString(R.string.password_repeat_message))
        if (password != passwordRepeat)
            return Pair(false, resources.getString(R.string.passwords_do_not_match_message))
        else
            return Pair(true, "")
    }

    /**
     * Sign in dialog
     */

    fun showSignInDialog() {
        val dialogBinding = SignInWindowBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.authorisation))
            .setMessage(resources.getString(R.string.authorisation_message))
            .setView(dialogBinding.root)
            .setNegativeButton(resources.getString(R.string.action_cancel)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.action_confirm), null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val username = dialogBinding.usernameEditText.text.toString()
                val password = dialogBinding.passwordEditText.text.toString()
                val (checkResult, checkMessage) = checkAuthorisationForm(username, password)
                if (!checkResult) {
                    Toast.makeText(context, checkMessage, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.login(username, password)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    /**
     * Auth data verification
     */

    private fun checkAuthorisationForm(
        username: String, password: String
    ): Pair<Boolean, String> {
        if (username.isBlank())
            return Pair(false, resources.getString(R.string.username_message))
        if (password.isBlank())
            return Pair(false, resources.getString(R.string.password_message))
        else
            return Pair(true, "")
    }
}