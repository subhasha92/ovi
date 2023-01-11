package org.ovi.common

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.ovi.data.pref.Preferences
import org.ovi.di.networkModule
import org.ovi.di.preferenceModule
import org.ovi.di.repositoryModule
import org.ovi.di.viewModelModule


private var appContext: OviApp? = null

class OviApp : Application() {

    companion object {
        fun getAppContext() = appContext!!
    }

    override fun onCreate() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                p0.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            }

            override fun onActivityStarted(p0: Activity) {

            }

            override fun onActivityResumed(p0: Activity) {

            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(p0: Activity) {

            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

            }

            override fun onActivityDestroyed(p0: Activity) {

            }
        })
        super.onCreate()

        appContext = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Preferences.init(this)
        startKoin {
            androidContext(this@OviApp)
            modules(listOf(preferenceModule, viewModelModule, networkModule, repositoryModule))
        }
    }

}