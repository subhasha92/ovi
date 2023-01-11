package org.ovi.feature.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivitySplashScreenBinding
import org.ovi.feature.auth.view.LoginActivity
import org.ovi.feature.events.model.EventDetailsMode
import org.ovi.feature.events.view.activity.EventsDetailActivity
import org.ovi.feature.forgetpassword.view.SetNewPasswordActivity
import org.ovi.feature.home.view.activity.HomeActivity
import java.lang.ref.WeakReference

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    override fun setupView() {

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.splash_fade_in)
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
//                if(pref.isLoggedIn){

                val appLinkIntent = intent
                val appLinkAction = appLinkIntent.action
                val appLinkData = appLinkIntent.data

                if (Intent.ACTION_VIEW == appLinkAction) {
                    if (appLinkData?.path?.endsWith("/login") == true) {
                        LoginActivity.present(this@SplashScreenActivity)

                    } else if (appLinkData?.path?.startsWith("/memberuserDesktop/login/resetpassword") == true) {
                        val tok = appLinkData.getQueryParameter("token").toString()
                        SetNewPasswordActivity.present(this@SplashScreenActivity, tok)

                    } else if (appLinkData?.path?.startsWith("/memberuserDesktop/profile/eventview") == true) {
                        val id = appLinkData.getQueryParameter("value")?.toInt()!!
                        val mode = appLinkData.getQueryParameter("type")?.toString()
                        EventsDetailActivity.present(
                            WeakReference<Activity>(this@SplashScreenActivity),
                            when (mode) {
                                EventDetailsMode.CANCEL.value -> EventDetailsMode.CANCEL
                                EventDetailsMode.ACCEPT.value -> EventDetailsMode.ACCEPT
                                EventDetailsMode.ATTENDED.value -> EventDetailsMode.ATTENDED
                                EventDetailsMode.REGISTER.value -> EventDetailsMode.REGISTER
                                else -> EventDetailsMode.ACCEPT
                            }, id
                        )
                    }
                } else {
                    HomeActivity.present(this@SplashScreenActivity)
                }
                finish()

            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        binding.text.startAnimation(fadeIn)

    }

    override fun bindViewModel() {

    }

    override fun getViewBinding() = ActivitySplashScreenBinding.inflate(layoutInflater)

    override fun onClick(view: View) {

    }
}