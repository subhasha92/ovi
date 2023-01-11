package org.ovi.feature.register.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.view.View
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityCongratsAccountBinding
import org.ovi.feature.auth.view.LoginActivity
import org.ovi.feature.home.view.activity.HomeActivity
import org.ovi.util.extensions.setCustomTypefaceSpanBold
import org.ovi.util.extensions.setForegroundColorSpan
import java.lang.ref.WeakReference

class CongratsAccountActivity : BaseActivity<ActivityCongratsAccountBinding>() {

    companion object {
        fun present(context: Context) {
            context.startActivity(Intent(context, CongratsAccountActivity::class.java))
        }
    }

    override fun setupView() {

        binding.btnContinue.setOnClickListener(onClickListener)
        binding.tvGoHome.setOnClickListener(onClickListener)
        binding.toolbar.setBackListener { LoginActivity.present(this) }
        binding.toolbar.setEndIconListener { HomeActivity.present(this,true) }

       binding.tvEmail.text= setTextSpan(getString(R.string.this_is_your_registration_id_james_ovi_com,pref.email),pref.email.toString())

    }

    private fun setTextSpan(text: String, mainText: String): SpannableString {

        val str = SpannableString(text).apply {
            setForegroundColorSpan(
                R.color.light_brown,
                this@CongratsAccountActivity, text.indexOf(mainText), text.length
            )
            setCustomTypefaceSpanBold(
                R.font.opensans_semibold,
                this@CongratsAccountActivity,
                text.indexOf(mainText),
                text.length
            )
        }
        return str
    }

    override fun bindViewModel() {

    }

    override fun getViewBinding() = ActivityCongratsAccountBinding.inflate(layoutInflater)

    override fun onClick(view: View) {
        when (view) {
            binding.btnContinue -> {
                AdditionalQuestActivity.present(WeakReference(this))
            }
            binding.tvGoHome->{
                HomeActivity.present(this,false)
            }
        }
    }
}