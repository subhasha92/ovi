package org.ovi.feature.register.view.fragments

import android.text.InputType
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentRegPhoneBinding
import org.ovi.feature.register.model.RegKey
import org.ovi.feature.register.model.RegisterRequest
import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.feature.register.viewmodel.ValidationViewModel
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.show
import org.ovi.widget.OviSnackBar

class RegPhoneFragment : BaseFragment<FragmentRegPhoneBinding>() {

    private val vm: ValidationViewModel by viewModel()

    override fun onClick(view: View) {
        when (view) {
            binding.include.btnNext -> {
                if (isValid()) {
                   checkPhone()
                }
            }
            binding.include.tvSkip->{ (requireActivity() as RegisterActivity).movePager()}
        }
    }
    private fun checkPhone() {
        vm.validPhone(RegisterRequest(phone =   getString(R.string.prefix_number).plus(binding.include.etText.text.trim())))
    }
    override fun getViewBinding() = FragmentRegPhoneBinding.inflate(layoutInflater)

    override fun setupView() {
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        with(binding.include) {
            tvTitle.text = getString(R.string.please_enter_phone_numbre)
            etText.label = "Phone number"
            etText.hint="Enter the Phone number"
            etText.setMaxLength(10)
            etText.setPrefixText(getString(R.string.prefix_number))
            tvText.gone()
            etText.setInputType(InputType.TYPE_CLASS_NUMBER)
            btnNext.setOnClickListener(onClickListener)
//            tvSkip.show()
            tvSkip.setOnClickListener(onClickListener)
        }
    }

    private fun onSuccess(exist:Boolean) {
        if (!exist) {
            (requireActivity() as RegisterActivity).storeRegisterRequest(
                RegKey.PHONE.value,
                getString(R.string.prefix_number).plus(binding.include.etText.text.trim())
            )
            (requireActivity() as RegisterActivity).movePager()
        }else
            showSnackBar(getString(R.string.phone_number_already_exist),OviSnackBar.SnackType.FAILURE,binding.include.btnNext)
    }


    override fun bindViewModel() {
        vm.valid bindTo {
            when(it){
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()

                }
                is ResultOf.Success -> {
                    it.value.data?.phone_exists?.let { it1 -> onSuccess(it1) }
                }
            }
        }
    }

    private fun isValid(): Boolean {

        when {
            binding.include.etText.text.isEmpty() -> {
                binding.include.etText.setError("Phone number is required")
                return false
            }
            binding.include.etText.text.length < 10 -> {
                binding.include.etText.setError("Phone number should be minimum 10 digit")
                return false
            }

            binding.include.etText.text.length in 11..12 -> {
                binding.include.etText.setError("Phone number should be with international code")
                return false
            }


        }
        return true
    }
}