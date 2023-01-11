package org.ovi.feature.auth.view


import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isEmpty
import androidx.core.widget.addTextChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityLoginBinding
import org.ovi.feature.auth.model.LoginRequest
import org.ovi.feature.auth.viewmodel.AuthViewModel
import org.ovi.feature.events.model.EventDetailsMode
import org.ovi.feature.events.view.activity.EVENT_ID
import org.ovi.feature.events.view.activity.EventsDetailActivity
import org.ovi.feature.forgetpassword.view.ForgetPasswordActivity
import org.ovi.feature.home.view.activity.HomeActivity
import org.ovi.feature.profile.view.activity.RETURN_RESULT
import org.ovi.feature.register.view.activity.ChooseRoleActivity
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.setCustomTypefaceSpanBold
import org.ovi.util.extensions.setForegroundColorSpan
import org.ovi.util.extensions.show
import org.ovi.widget.OviSnackBar
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

const val LOGINREQUIRED = "loginRequired"

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val vm: AuthViewModel by viewModel()

    companion object {
        fun present(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }

        fun createIntent(
            activityWeakReference: WeakReference<Activity>,
            isLoginRequire:Boolean=true
        ): Intent {
            return Intent(
                activityWeakReference.get(),
                LoginActivity::class.java
            ).putExtra(LOGINREQUIRED,isLoginRequire)
        }
    }

    private val loginRequired by lazy {
        intent.getBooleanExtra(LOGINREQUIRED, false)
    }

    override fun setupView() {

        binding.incLogin.btnLogin.setEnable(false)
        binding.incLogin.btnLogin.setOnClickListener(onClickListener)
        binding.ivBack.setOnClickListener(onClickListener)

        binding.incLogin.tvSignUP.text = setSignUpText()

        binding.incLogin.tvForgetPass.text = setTextSpan(
            getString(R.string.forgot_your_password_request_a_new_one),
            "Request a new one."
        )
        binding.incLogin.tvLoginWithNumber.text =
            setTextSpan(getString(R.string.log_in_with_phone_number), "Phone number")

//        Log.d(TAG, "setupView: ${SimpleDateFormat("yyyy-MM-dd").format(Date())}")
//        testCrash()
    }

    private fun testCrash() {
        // Creates a button that mimics a crash when pressed
        val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(
            crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun setSignUpText(): CharSequence {

        val text = getString(R.string.dont_have_an_account_join_ovi_network)
        val subText = "Join OVI network"

        val str = SpannableString(text).apply {
            setForegroundColorSpan(
                R.color.light_blue,
                this@LoginActivity, text.indexOf(subText), text.length
            )
            setCustomTypefaceSpanBold(
                R.font.opensans_semibold,
                this@LoginActivity,
                text.indexOf(subText),
                text.length
            )
        }
        return str
    }

    private fun setTextSpan(text: String, mainText: String): SpannableString {
        val str = SpannableString(text).apply {
            setForegroundColorSpan(
                R.color.light_brown,
                this@LoginActivity, text.indexOf(mainText), text.length
            )
            setCustomTypefaceSpanBold(
                R.font.opensans_semibold,
                this@LoginActivity,
                text.indexOf(mainText),
                text.length
            )
        }
        return str
    }


    override fun bindViewEvents() {
        super.bindViewEvents()
        binding.incLogin.etPassword.editText?.addTextChangedListener {
            if (isValid())
                binding.incLogin.btnLogin.setEnable(true)
        }
        binding.incLogin.tvSignUP.setOnClickListener(onClickListener)
        binding.incLogin.btnLogin.setOnClickListener(onClickListener)
        binding.inNoAcc.btnYes.setOnClickListener(onClickListener)
        binding.incLogin.tvForgetPass.setOnClickListener(onClickListener)
        binding.incLogin.tvLoginWithNumber.setOnClickListener(onClickListener)
    }

    private fun isValid(): Boolean {
        return when {
            binding.incLogin.etEmail.text.isEmpty() -> {
                binding.incLogin.etEmail.setError("Email can't be blank")
                false
            }

            binding.incLogin.etPassword.isEmpty() -> {
                binding.incLogin.etPassword.setError("Password can't be blank")
                false
            }
            binding.incLogin.etPassword.text.length < 8 -> {
                binding.incLogin.etPassword.setError("Password Length should be more than 8")
                false
            }
            else -> {
                binding.incLogin.etEmail.setError(null)
                binding.incLogin.etPassword.setError(null)
                true
            }
        }
    }

    override fun bindViewModel() {

        vm.login bindTo {
            when (it) {
                is ResultOf.Empty -> {
                }
                is ResultOf.Failure -> {
                    showSnackBar(
                        it.message,
                        OviSnackBar.SnackType.FAILURE,
                        binding.incLogin.tvLoginWithNumber
                    )
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    if (it.value.statusCode == 400) {
                        binding.incLogin.root.gone()
                        binding.inNoAcc.root.show()
                    } else {
                        if (loginRequired) {
                            val result = Intent()
                            result.putExtra(RETURN_RESULT, true)
                            setResult(RESULT_OK, result)
                            finish()
                        } else
                            HomeActivity.present(this)
                    }
                }
            }
        }

    }

    override fun getViewBinding() = ActivityLoginBinding.inflate(layoutInflater)

    override fun onClick(view: View) {
        with(binding) {
            when (view) {
                incLogin.btnLogin -> {
                    if (isValid()) {
                        val login = LoginRequest(
                            email = binding.incLogin.etEmail.editText?.text.toString(),
                            password = binding.incLogin.etPassword.editText?.text.toString(),
                            device_token = pref.fireToken
                        )
                        vm.doLogin(login)
                    }
                }
                inNoAcc.btnYes -> {
                    ChooseRoleActivity.present(this@LoginActivity)
                }
                incLogin.tvForgetPass -> {
                    ForgetPasswordActivity.present(this@LoginActivity)
                }
                incLogin.tvLoginWithNumber -> {
                    AuthMobileLoginActivity.present(this@LoginActivity)
                }
                incLogin.tvSignUP -> {
                    ChooseRoleActivity.present(this@LoginActivity)
                }
                binding.ivBack -> {
                    finish()
                }
            }
        }
    }

}