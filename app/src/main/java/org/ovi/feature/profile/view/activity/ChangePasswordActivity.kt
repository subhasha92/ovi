package org.ovi.feature.profile.view.activity

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.View
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityChangePasswordBinding
import org.ovi.feature.auth.model.ChangePasswordRequest
import org.ovi.feature.auth.viewmodel.AuthViewModel
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone
import org.ovi.widget.OviSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference
import java.util.regex.Pattern

class ChangePasswordActivity :BaseActivity<ActivityChangePasswordBinding>() {

    private val vm:AuthViewModel by viewModel()

    companion object{
        fun present(activityWeakReference: WeakReference<Activity>){
            activityWeakReference.get()?.startActivity(Intent(activityWeakReference.get(),ChangePasswordActivity::class.java))
        }
    }

    override fun setupView() {

        binding.toolbar.setBackListener { finish() }
        binding.incForget.tvCancel.setOnClickListener(onClickListener)
        binding.incForget.btnReset.setOnClickListener(onClickListener)

    }

    override fun bindViewModel() {

        vm.change bindTo {
            when(it){
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message,OviSnackBar.SnackType.FAILURE,binding.incForget.btnReset)
//                    Log.e(TAG, "bindViewModel: ${it.message}" )
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    binding.incForget.etCurrent.text=""
                    binding.incForget.etNew.text=""
                    binding.incForget.etNewConfirm.text=""
                    showSnackBar(it.value.message,OviSnackBar.SnackType.SUCCESS,binding.incForget.btnReset)
                    runDelayed(2000){
                        finish()
                    }

                }
            }
        }

    }

    override fun getViewBinding()=ActivityChangePasswordBinding.inflate(layoutInflater)

    override fun onClick(view: View) {

        when(view){
            binding.incForget.tvCancel->{finish()}
            binding.incForget.btnReset->{
                if (isValid() && isValidPass()){
                    val request=ChangePasswordRequest(
                       oldPassword= binding.incForget.etCurrent.text,
                        newPassword = binding.incForget.etNew.text
                    )
                    vm.changePassword(request)
                }
            }
        }
    }

    private fun isValid():Boolean{
        with(binding.incForget) {
            return when {

                etCurrent.text.isEmpty()->{
                    etCurrent.setError("Field can't be blank")
                    false
                }

                etNew.text.isEmpty()->{
                    etNew.setError("Field can't be blank")
                    false
                }

                etNewConfirm.text.isEmpty()->{
                    etNewConfirm.setError("Field can't be blank")
                    false
                }

                etNew.text != etNewConfirm.text->{
                    etNewConfirm.setError("Should match new password")
                    false
                }
                else -> true
            }
        }
    }

    private fun isValidPass(): Boolean {

        val password: String = binding.incForget.etNew.text

        val passwordInstruction=getString(R.string.password_instruction)
        // check for pattern
        val uppercase: Pattern = Pattern.compile("[A-Z]")
        val lowercase: Pattern = Pattern.compile("[a-z]")
        val digit: Pattern = Pattern.compile("[0-9]")
        val special: Pattern = Pattern.compile("[$&+,:;=?@#|'<>.-^*()%!]")

        // if lowercase character is not present
        if (!lowercase.matcher(password).find()) {
            binding.incForget.etNew.setError(passwordInstruction)
            return false
        }
        // if uppercase character is not present
        if (!uppercase.matcher(password).find()) {
            binding.incForget.etNew.setError(passwordInstruction)
            return false
        }
        // if digit is not present
        if (!digit.matcher(password).find()) {
            binding.incForget.etNew.setError(passwordInstruction)
            return false
        }

        // if special char is not present
        if (!special.matcher(password).find()) {
            binding.incForget.etNew.setError(passwordInstruction)
            return false
        }

        // if password length is less than 8
        if (password.length < 8) {
            binding.incForget.etNew.setError(passwordInstruction)
            return false
        }

        return true
    }
}