package org.ovi.feature.register.view.fragments

import android.view.View
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentRegEnterNameBinding
import org.ovi.feature.register.model.RegKey
import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.util.extensions.gone
import java.util.regex.Pattern

class RegEnterNameFragment : BaseFragment<FragmentRegEnterNameBinding>() {

    override fun onClick(view: View) {
        with(binding) {
            when (view) {
                btnNext -> {
                    if (isValid() && isValidPass()) {
                        (requireActivity() as RegisterActivity).movePager()
                        (requireActivity() as RegisterActivity).storeRegisterRequest(
                            RegKey.NAME.value,
                            binding.etName.text
                        )
                        (requireActivity() as RegisterActivity).storeRegisterRequest(
                            RegKey.PASSWORD.value,
                            binding.etPassword.text
                        )
                    }
                }
            }
        }
    }

    override fun getViewBinding() = FragmentRegEnterNameBinding.inflate(layoutInflater)

    private fun isValid(): Boolean {
        return when {
            binding.etName.text.isEmpty() -> {
                binding.etName.setError("Can't be blank")
                false
            }
            binding.etPassword.text.isEmpty() -> {
                binding.etPassword.setError("Can't be blank")
                false
            }
            else -> true
        }
    }

    override fun setupView() {
        binding.tvText.gone()
        binding.btnNext.setOnClickListener(onClickListener)
    }

    override fun bindViewModel() {

    }

    private fun isValidPass(): Boolean {

        val password: String = binding.etPassword.text

        val passwordInstruction=getString(R.string.password_instruction)
        // check for pattern
        val uppercase: Pattern = Pattern.compile("[A-Z]")
        val lowercase: Pattern = Pattern.compile("[a-z]")
        val digit: Pattern = Pattern.compile("[0-9]")
        val special: Pattern = Pattern.compile("[$&+,:;=?@#|'<>.-^*()%!]")

        // if lowercase character is not present
        if (!lowercase.matcher(password).find()) {
            binding.etPassword.setError(passwordInstruction)
            return false
        }
        // if uppercase character is not present
        if (!uppercase.matcher(password).find()) {
            binding.etPassword.setError(passwordInstruction)
            return false
        }
        // if digit is not present
        if (!digit.matcher(password).find()) {
            binding.etPassword.setError(passwordInstruction)
            return false
        }

          // if special char is not present
        if (!special.matcher(password).find()) {
            binding.etPassword.setError(passwordInstruction)
            return false
        }

        // if password length is less than 8
        if (password.length < 8) {
            binding.etPassword.setError(passwordInstruction)
            return false
        }

        return true
    }

}