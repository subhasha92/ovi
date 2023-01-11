package org.ovi.feature.forgetpassword.view

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.util.Log
import android.view.View
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityForgetPasswordBinding
import org.ovi.feature.auth.viewmodel.AuthViewModel
import org.ovi.feature.forgetpassword.model.ForgetRequest
import org.ovi.network.ResultOf
import org.ovi.util.extensions.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgetPasswordActivity : BaseActivity<ActivityForgetPasswordBinding>() {

    private val vm: AuthViewModel by viewModel()

    companion object {
        fun present(context: Context) {
            context.startActivity(Intent(context, ForgetPasswordActivity::class.java))
        }
    }

    override fun setupView() {

    }

    override fun bindViewEvents() {
        super.bindViewEvents()
        binding.ivBack.setOnClickListener(onClickListener)
        binding.incForget.btnSend.setOnClickListener(onClickListener)
        binding.incForget.tvCancel.setOnClickListener(onClickListener)
        binding.incCheck.tvClick.setOnClickListener(onClickListener)
    }

    override fun bindViewModel() {
        vm.forget bindTo {
            when (it) {
                is ResultOf.Success -> {
                    if (it.value.statusCode == 201) {
                        binding.incForget.root.gone()
                        setSpannableString(binding.incForget.etEmail.editText?.text.toString())
                        binding.incCheck.root.show()

                    } else {
                        showToast(it.value.message)
                    }
                }
                is ResultOf.Empty -> {
                }
                is ResultOf.Failure -> {
//                    Log.e(TAG, "bindViewModel: ${it.message}" )
                    showToast(it.message)
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
            }
        }

    }

    private fun setSpannableString(email: String) {
        val text = getString(
            R.string.we_sent_a_password_reset_link_to_jan_hotmail_com_with_all_the_instructions,
            email
        )

        val spn = SpannableString(text).apply {
            setCustomTypefaceSpanBold(
                R.font.opensans_bold,
                this@ForgetPasswordActivity,
                text.indexOf(email),
                text.indexOf(email) + email.length
            )
        }
        binding.incCheck.tvText.text = spn

        val text2 = getString(R.string.didn_t_receive_the_email_click_to_resend)
        val spn2 = SpannableString(text2).apply {
            setForegroundColorSpan(
                R.color.light_brown,
                this@ForgetPasswordActivity, text2.indexOf("Click"), text2.length
            )
        }
        binding.incCheck.tvClick.text = spn2

    }

    override fun getViewBinding() = ActivityForgetPasswordBinding.inflate(layoutInflater)

    override fun onClick(view: View) {

        with(binding) {
            when (view) {
                ivBack -> {
                  goBack()
                }
                incForget.btnSend -> {
                    if (binding.incForget.etEmail.editText?.text?.isNotEmpty() == true) {
                        vm.forgetPassword(ForgetRequest(binding.incForget.etEmail.editText?.text.toString()))
                    }
                }
                incForget.tvCancel -> {
                    finish()
                }
                incCheck.tvClick -> {
                    goBack()
//                    SetNewPasswordActivity.present(this@ForgetPasswordActivity)
                }
            }
        }


    }

    fun goBack(){
        with(binding) {
            if (incForget.root.isVisible())
                finish()
            else {
                incForget.root.show()
                incCheck.root.gone()
            }
        }
    }
}