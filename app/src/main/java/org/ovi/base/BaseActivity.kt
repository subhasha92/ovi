package org.ovi.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ovi.R
import org.ovi.data.pref.OVIPreferences
import org.ovi.util.TransitionHandler
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.hideSoftKeyboard
import org.ovi.util.extensions.showToast
import org.ovi.widget.OviSnackBar


abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    lateinit var binding: B
    private var snackBar: OviSnackBar? = null
    val pref by lazy { OVIPreferences() }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)

        preInit()
        binding = getViewBinding()
        setContentView(binding.root)
        init()
        setupView()
        bindViewEvents()
        bindViewModel()
    }

    private fun init() {
    }

    protected open fun preInit() {

    }

    protected abstract fun setupView()
    protected open fun bindViewEvents() {
        requireNotNull(binding.root).setOnClickListener {
            hideSoftKeyboard()
        }
    }

    protected abstract fun bindViewModel()
    abstract fun getViewBinding(): B


    protected val onClickListener = View.OnClickListener {
        hideSoftKeyboard()
        onClick(it)
    }

    protected abstract fun onClick(view: View)

    protected inline infix fun <T> Flow<T>.bindTo(crossinline action: (T) -> Unit) {
        with(this) {
            lifecycleScope.launch {
                repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                    collect { action(it) }
                }
            }
        }
    }

    protected fun showSnackBar(message: String?, type: OviSnackBar.SnackType, view: View? = null) {
        binding.root.let {
            snackBar = OviSnackBar
                .Builder()
                .type(type)
                .message(message)
                .setCallBack(snackListener)
                .make(it)
                .setAnchorView(view)
                .showSnack()
        }
    }

    private val snackListener = object : OviSnackBar.Builder.ISnackListener {
        override fun onClosed(view: View) {
            snackBar?.dismiss()
        }
    }
    var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
            return
        }
        doubleBackToExitPressedOnce = true
        showToast("Please click BACK again to exit from app")
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)

    }

    protected fun applyProdDetailNavigation() {
        window.navigationBarColor = getOviColor(R.color.navigation_brown)
    }

    protected fun applyAuthNavigation() {
        window.navigationBarColor = getOviColor(R.color.navigation_brown)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(org.ovi.util.LocaleHelper.onAttach(base))
    }

    open fun getResources(context: Context?): Resources? {
        return org.ovi.util.LocaleHelper.onAttach(context).resources
    }

    protected fun setSlideTransition() {
        window?.run {
            enterTransition = TransitionHandler.slideTransition
            reenterTransition = TransitionHandler.slideTransition
            exitTransition = TransitionHandler.slideTransition
        }
    }

    protected fun setWindowTransition() {
        window?.run {
            enterTransition = TransitionHandler.getRandomAnim()
            reenterTransition = TransitionHandler.fadeTransition
            exitTransition = TransitionHandler.fadeTransition
        }
    }

    fun runDelayed(delayMilliSec: Long, job: suspend () -> Unit) =
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                delay(delayMilliSec)
                withContext(Dispatchers.Main) {
                    job.invoke()
                }
            }
        }


    protected fun getTransitionOptions(): Bundle {
        val pairs = org.ovi.util.TransitionHelper.createSafeTransitionParticipants(this, true)
        return ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()!!
    }

}