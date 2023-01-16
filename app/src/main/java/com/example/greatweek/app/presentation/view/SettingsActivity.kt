package com.example.greatweek.app.presentation.view

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.greatweek.R
import com.example.greatweek.databinding.SignInWindowBinding
import com.example.greatweek.databinding.SignUpWindowBinding


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val myPref: Preference? = findPreference("Login") as Preference?
            myPref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                showSignUpDialog()
                true
            }
        }

        /**
         * Sign up dialog
         */

        private fun showSignUpDialog() {
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

            val email = dialogBinding.emailEditText
            val password = dialogBinding.passwordEditText
            val passwordRepeat = dialogBinding.repeatPasswordEditText

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    if (email.text.isBlank()) {
                        showToast(requireContext(), resources.getString(R.string.email_message))
                        return@setOnClickListener
                    }
                    if (password.text.isBlank()) {
                        showToast(requireContext(), resources.getString(R.string.password_message))
                        return@setOnClickListener
                    }
                    if (passwordRepeat.text.isBlank()) {
                        showToast(requireContext(), resources.getString(R.string.password_repeat_message))
                        return@setOnClickListener
                    }
                    // TODO регистрация
                    dialog.dismiss()
                }
            }
            dialog.show()
        }

        /**
         * Sign in dialog
         */

        private fun showSignInDialog() {
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

            val email = dialogBinding.emailEditText
            val password = dialogBinding.passwordEditText

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    if (email.text.isBlank()) {
                        showToast(requireContext(), resources.getString(R.string.email_message))
                        return@setOnClickListener
                    }
                    if (password.text.isBlank()) {
                        showToast(requireContext(), resources.getString(R.string.password_message))
                        return@setOnClickListener
                    }
                    // TODO авторизация
                    dialog.dismiss()
                }
            }
            dialog.show()
        }

        private fun showToast(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}