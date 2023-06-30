package com.example.greatweek.app.presentation.screens.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.greatweek.R
import com.example.greatweek.app.App
import com.example.greatweek.app.presentation.RequestState
import com.example.greatweek.app.presentation.ViewModelFactory
import com.example.greatweek.databinding.DialogSignInBinding
import com.example.greatweek.databinding.DialogSignUpBinding
import com.example.greatweek.databinding.FragmentRootSettingsBinding
import com.example.greatweek.domain.model.network.UserSignIn
import com.example.greatweek.domain.model.network.UserSignUp
import com.example.greatweek.domain.utils.Either
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    private var _binding: FragmentRootSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as App).appComponent.inject(this)
    }

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
        binding.apply {
            settingsFragment = this@SettingsFragment
            settingsViewModel = viewModel
        }

        viewModel.signInRequestState.collectRequestState(
            onError = { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() },
            onSuccess = {  }
        )

        viewModel.signUpRequestState.collectRequestState(
            onError = { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() },
            onSuccess = {  }
        )

        viewModel.authState.observe(viewLifecycleOwner) {
            if (it) {
                binding.groupUserInfo.isVisible = true
                binding.groupAuthentication.isVisible = false
            } else {
                binding.groupUserInfo.isVisible = false
                binding.groupAuthentication.isVisible = true
            }
        }

        viewModel.username.observe(viewLifecycleOwner) {
            binding.usernameTextView.text = it
        }
    }

    // Sign up dialog
    fun showSignUpDialog() {
        val dialogBinding = DialogSignUpBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setNegativeButton(resources.getString(R.string.action_cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
            .setPositiveButton(resources.getString(R.string.action_confirm), null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val username = dialogBinding.usernameEditText.text.toString()
                val email = dialogBinding.emailEditText.text.toString()
                val password = dialogBinding.passwordEditText.text.toString()
                val passwordRepeat = dialogBinding.repeatPasswordEditText.text.toString()
                when (val check = viewModel.checkSignUpForm(
                    username, email, password, passwordRepeat
                )) {
                    is Either.Left -> {
                        Toast.makeText(context, resources.getString(check.value), Toast.LENGTH_SHORT).show()
                    }
                    is Either.Right -> {
                        viewModel.signUp(UserSignUp(username = username, password = password, email = email))
                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.show()
    }

    // Sign in dialog
    fun showSignInDialog() {
        val dialogBinding = DialogSignInBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setNegativeButton(resources.getString(R.string.action_cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
            .setPositiveButton(resources.getString(R.string.action_confirm), null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val username = dialogBinding.usernameEditText.text.toString()
                val password = dialogBinding.passwordEditText.text.toString()
                when (val check = viewModel.checkSignInForm(username, password)) {
                    is Either.Left -> {
                        Toast.makeText(context, resources.getString(check.value), Toast.LENGTH_SHORT).show()
                    }
                    is Either.Right -> {
                        viewModel.signIn(UserSignIn(username = username, password = password))
                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.show()
    }

    private fun <T> StateFlow<RequestState<T>>.collectRequestState(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        state: ((RequestState<T>) -> Unit)? = null,
        onError: ((error: String) -> Unit),
        onSuccess: ((data: T) -> Unit)
    ) {
        collectFlowSafely(lifecycleState) {
            this.collect {
                state?.invoke(it)
                when (it) {
                    is RequestState.Idle -> {}
                    is RequestState.Loading -> {}
                    is RequestState.Error -> onError.invoke(it.error)
                    is RequestState.Success -> onSuccess.invoke(it.data)
                }
            }
        }
    }

    private fun collectFlowSafely(
        lifecycleState: Lifecycle.State,
        collect: suspend () -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
                collect()
            }
        }
    }
}