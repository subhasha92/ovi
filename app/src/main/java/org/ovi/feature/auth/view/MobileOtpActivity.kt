package org.ovi.feature.auth.view

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityMobileOtpBinding
import org.ovi.feature.auth.model.VerifyOtpRequest
import org.ovi.feature.auth.viewmodel.AuthViewModel
import org.ovi.feature.home.view.activity.HomeActivity
import org.ovi.feature.register.view.activity.ChooseRoleActivity
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.setCustomTypefaceSpanBold
import org.ovi.util.extensions.setForegroundColorSpan
import org.ovi.widget.OviSnackBar

const val MOBILE = "mobile"

class MobileOtpActivity : BaseActivity<ActivityMobileOtpBinding>() {
    private val vm: AuthViewModel by viewModel()

    companion object {
        fun present(context: Context, mobile: String) {
            context.startActivity(
                Intent(context, MobileOtpActivity::class.java)
                    .putExtra(MOBILE, mobile)
            )
        }
    }

    private val mobile by lazy {
        intent.getStringExtra(MOBILE)
    }

    override fun setupView() {
        binding.btnLogin.setOnClickListener(onClickListener)
        binding.ivBack.setOnClickListener(onClickListener)
        binding.tvSignUP.text = setSignUpText()
        binding.tvSignUP.setOnClickListener(onClickListener)
    }

    override fun bindViewModel() {
        vm.verifyOtp bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE, binding.btnLogin)
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    HomeActivity.present(this)
                }
            }
        }
    }

    private fun setSignUpText(): CharSequence {

        val text = getString(R.string.dont_have_an_account_join_ovi_network)
        val subText = "Join OVI network"

        val str = SpannableString(text).apply {
            setForegroundColorSpan(
                R.color.light_blue,
                this@MobileOtpActivity, text.indexOf(subText), text.length
            )
            setCustomTypefaceSpanBold(
                R.font.opensans_semibold,
                this@MobileOtpActivity,
                text.indexOf(subText),
                text.length
            )
        }
        return str
    }

    override fun getViewBinding() = ActivityMobileOtpBinding.inflate(layoutInflater)

    override fun onClick(view: View) {
        when (view) {
            binding.btnLogin -> {
                if (binding.etOtpBox.getText().isNotEmpty()) {
                    vm.verifyOtp(
                        VerifyOtpRequest(
                            phone = mobile,
                            otp = binding.etOtpBox.getText().toInt()
                        )
                    )
                } else {
                    showSnackBar(
                        "Otp can't be blank",
                        OviSnackBar.SnackType.VALIDATION,
                        binding.btnLogin
                    )
                }
            }
            binding.ivBack -> {
                finish()
            }
            binding.tvSignUP -> {
                ChooseRoleActivity.present(this@MobileOtpActivity)
            }
        }
    }
}
