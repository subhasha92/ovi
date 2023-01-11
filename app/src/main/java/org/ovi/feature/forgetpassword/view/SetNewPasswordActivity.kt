package org.ovi.feature.forgetpassword.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivitySetNewPasswordBinding
import org.ovi.feature.auth.view.LoginActivity
import org.ovi.feature.auth.viewmodel.AuthViewModel
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.R
import org.ovi.feature.forgetpassword.model.ResetRequest
import org.ovi.network.ResultOf
import org.ovi.widget.OviSnackBar
import java.util.regex.Pattern

const val TOKEN="token"
class SetNewPasswordActivity:BaseActivity<ActivitySetNewPasswordBinding>() {

    private val vm:AuthViewModel by viewModel()
    private val stringKey by lazy {
        intent.getStringExtra(TOKEN)
    }
    companion object{
        fun present(context: Context, tok: String){
            context.startActivity(Intent(context,SetNewPasswordActivity::class.java).putExtra(TOKEN,tok))
        }
    }
    override fun setupView() {

//        val appLinkIntent = intent
//        val appLinkAction = appLinkIntent.action
//        val appLinkData = appLinkIntent.data
//
//        if (Intent.ACTION_VIEW == appLinkAction) {
//            stringKey=appLinkData?.getQueryParameter("token").toString()
//        }

    }

    override fun bindViewEvents() {
        super.bindViewEvents()
        binding.incSetPassword.btnReset.setOnClickListener(onClickListener)
        binding.incSetPassword.tvCancel.setOnClickListener(onClickListener)
        binding.incPasswordReset.ivBack.setOnClickListener(onClickListener)
        binding.incPasswordReset.btnContinue.setOnClickListener(onClickListener)
    }


    override fun bindViewModel() {

        vm.reset bindTo {
            when(it){
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message,OviSnackBar.SnackType.FAILURE,binding.incSetPassword.btnReset)
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    binding.incPasswordReset.root.show()
                    binding.incSetPassword.root.gone()
                }
            }
        }


    }

    override fun getViewBinding()=ActivitySetNewPasswordBinding.inflate(layoutInflater)

    override fun onClick(view: View) {
        when(view){
            binding.incSetPassword.btnReset->
            {
                if (ifValid())
                    vm.resetPassword(ResetRequest(
                        password = binding.incSetPassword.etNew.text,
                        token = stringKey.toString()
                    ))
            }
            binding.incSetPassword.tvCancel->{
                finish()
            }
            binding.incPasswordReset.ivBack->{
             finish()
            }
            binding.incPasswordReset.btnContinue->
            {
                LoginActivity.present(this@SetNewPasswordActivity)
            }
        }
    }

    private fun ifValid(): Boolean {

        return when{
            binding.incSetPassword.etNew.text.isEmpty()->{
                binding.incSetPassword.etNew.setError("Field can't be blank")
                false
            }
            binding.incSetPassword.etNewConfirm.text != binding.incSetPassword.etNew.text->{
                binding.incSetPassword.etNewConfirm.setError("Password not matching")
                false
            }

            else-> isValidPass()
        }

    }

    private fun isValidPass(): Boolean {

        val password: String = binding.incSetPassword.etNew.text

        val passwordInstruction=getString(R.string.password_instruction)
        // check for pattern
        val uppercase: Pattern = Pattern.compile("[A-Z]")
        val lowercase: Pattern = Pattern.compile("[a-z]")
        val digit: Pattern = Pattern.compile("[0-9]")
        val special: Pattern = Pattern.compile("[$&+,:;=?@#|'<>.-^*()%!]")

        // if lowercase character is not present
        if (!lowercase.matcher(password).find()) {
            binding.incSetPassword.etNew.setError(passwordInstruction)
            return false
        }
        // if uppercase character is not present
        if (!uppercase.matcher(password).find()) {
            binding.incSetPassword.etNew.setError(passwordInstruction)
            return false
        }
        // if digit is not present
        if (!digit.matcher(password).find()) {
            binding.incSetPassword.etNew.setError(passwordInstruction)
            return false
        }

        // if special char is not present
        if (!special.matcher(password).find()) {
            binding.incSetPassword.etNew.setError(passwordInstruction)
            return false
        }

        // if password length is less than 8
        if (password.length < 8) {
            binding.incSetPassword.etNew.setError(passwordInstruction)
            return false
        }

        return true
    }

}