package org.ovi.feature.home.view.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.ovi.BuildConfig
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityHomeBinding
import org.ovi.feature.auth.view.LoginActivity
import org.ovi.feature.profile.view.fragment.ProfileHolderFragment
import org.ovi.util.extensions.getOviColor
import org.ovi.util.navigation.KeepStateNavigator

const val flag = "registeration"

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    companion object {
        fun present(
            context: Context, flagType: Boolean = false
        ) {
            context.startActivity(
                Intent(
                    context,
                    HomeActivity::class.java
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }.putExtra(flag, flagType)
            )
        }
    }

    private val flagType by lazy { intent.getBooleanExtra(flag, false) }

    private var refreshStatus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        configBottomNav()
    }

    override fun setupView() {
    }

    fun getRefreshStatus(): Boolean {
        return refreshStatus
    }

    fun setRefreshStatus(status: Boolean) {
        refreshStatus = status
    }

    private fun configBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navigator =
            KeepStateNavigator(
                this, navHostFragment.childFragmentManager,
                R.id.nav_host_fragment
            )
        navController.navigatorProvider += navigator

        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.navigation_main)

        if (flagType)
            graph.startDestination = R.id.navigation_profile

        navController.graph = graph

        binding.bottomNav.apply {
            inflateMenu(R.menu.menu_main)
            setupWithNavController(navController)
            if (pref.isLoggedIn)
                setOnItemSelectedListener(navItemSelected)
            else
                setOnItemSelectedListener(navItemSelectedUnregistered)

        }
//        if (!pref.isLoggedIn)
//            binding.bottomNav.menu.forEach { it.isEnabled = false }


    }

    private val navItemSelected = NavigationBarView.OnItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_home -> {
                navController.navigate(R.id.navigation_home)
                binding.bottomNav.setBackgroundColor(getOviColor(R.color.navigation_brown))
            }
            R.id.navigation_event -> {
                navController.navigate(R.id.navigation_event)
                binding.bottomNav.setBackgroundColor(getOviColor(R.color.navigation_brown))
            }
            R.id.navigation_profile -> {
                navController.navigate(R.id.navigation_profile)
                binding.bottomNav.setBackgroundColor(getOviColor(R.color.navigation_brown))
            }
            R.id.navigation_menu -> {
                navController.navigate(R.id.navigation_menu)
                binding.bottomNav.setBackgroundColor(getOviColor(R.color.navigation_brown))
            }
        }
        true
    }


    private val navItemSelectedUnregistered = NavigationBarView.OnItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_home -> {

            }
            R.id.navigation_event -> {
                alertMessage()
            }
            R.id.navigation_profile -> {
                alertMessage()
            }
            R.id.navigation_menu -> {
                alertMessage()
            }
        }
        true
    }


    override fun bindViewModel() {
    }

    fun moveToProfileOrEvent(type: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                fragment as ProfileHolderFragment
                fragment.switchProEvent(type)
            }
        }
    }

    private fun alertMessage() {
        with(Dialog(this)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.custom_cofirmation_dialogue)

            val tvCancel = findViewById<AppCompatTextView>(R.id.tvNo)
            val tvYes = findViewById<AppCompatTextView>(R.id.tvYes)
            val title = findViewById<AppCompatTextView>(R.id.tvTitle)
            val messsage = findViewById<AppCompatTextView>(R.id.tvSubTitle)

            title.text = getString(R.string.registered_user_only)
            messsage.text = getString(R.string.please_register_login_to_view)

            tvCancel.setOnClickListener {
                dismiss()
            }
            tvYes.setOnClickListener {
                dismiss()
                LoginActivity.present(this@HomeActivity)
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

    fun setItemSelected(id: Int) {
        binding.bottomNav.selectedItemId = id
    }

    override fun getViewBinding() = ActivityHomeBinding.inflate(layoutInflater)

    override fun onClick(view: View) {

    }
}