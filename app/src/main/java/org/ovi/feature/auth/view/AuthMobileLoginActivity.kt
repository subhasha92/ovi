package org.ovi.feature.auth.view

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityAuthMobileLoginBinding
import org.ovi.feature.auth.model.GetMobileOtpRequest
import org.ovi.feature.auth.viewmodel.AuthViewModel
import org.ovi.feature.register.view.activity.ChooseRoleActivity
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.setCustomTypefaceSpanBold
import org.ovi.util.extensions.setForegroundColorSpan
import org.ovi.widget.OviSnackBar

class AuthMobileLoginActivity : BaseActivity<ActivityAuthMobileLoginBinding>() {

    private val vm: AuthViewModel by viewModel()

    companion object {
        fun present(context: Context) {
            context.startActivity(Intent(context, AuthMobileLoginActivity::class.java))
        }
    }

    override fun setupView() {
        binding.btnGetotp.setOnClickListener(onClickListener)
        binding.ivBack.setOnClickListener(onClickListener)
        binding.etMobile.setPrefixText(getString(R.string.prefix_number))
        binding.tvSignUP.text=setSignUpText()
        binding.tvSignUP.setOnClickListener(onClickListener)
    }

    override fun bindViewModel() {
        vm.getOtp bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE, binding.btnGetotp)
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    MobileOtpActivity.present(this, getString(R.string.prefix_number).plus(binding.etMobile.text.trim()))
                }
            }
        }
    }

    override fun getViewBinding() = ActivityAuthMobileLoginBinding.inflate(layoutInflater)

    override fun onClick(view: View) {
        when (view) {
            binding.btnGetotp -> {
                if (isValid())
                    vm.getOtp(GetMobileOtpRequest(getString(R.string.prefix_number).plus(binding.etMobile.text.trim())))
            }
            binding.ivBack -> {
                finish()
            }
            binding.tvSignUP->{
                ChooseRoleActivity.present(this@AuthMobileLoginActivity)
            }
        }

    }

    private fun setSignUpText(): CharSequence {

        val text = getString(R.string.dont_have_an_account_join_ovi_network)
        val subText = "Join OVI network"

        val str = SpannableString(text).apply {
            setForegroundColorSpan(
                R.color.light_blue,
                this@AuthMobileLoginActivity, text.indexOf(subText), text.length
            )
            setCustomTypefaceSpanBold(
                R.font.opensans_semibold,
                this@AuthMobileLoginActivity,
                text.indexOf(subText),
                text.length
            )
        }
        return str
    }

    private fun isValid(): Boolean {
        when {
            binding.etMobile.text.isEmpty() -> {
                binding.etMobile.setError("Phone number is required")
                return false
            }
            binding.etMobile.text.length < 10 -> {
                binding.etMobile.setError("Phone number should be minimum 10 digit")
                return false
            }
            binding.etMobile.text.length in 11..12 -> {
                binding.etMobile.setError("Phone number should be with international code")
                return false
            }
        }
        return true

    }

}