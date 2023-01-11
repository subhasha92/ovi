package org.ovi.feature.register.view.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityAdditionalQuestBinding
import org.ovi.feature.auth.view.LoginActivity
import org.ovi.feature.home.view.activity.HomeActivity
import org.ovi.feature.register.view.adapter.AddQuestAdapter
import org.ovi.feature.register.viewmodel.RegisterViewModel
import java.lang.ref.WeakReference

class AdditionalQuestActivity : BaseActivity<ActivityAdditionalQuestBinding>() {

    private val vm:RegisterViewModel by viewModel()

    companion object {
        fun present(activityWeakReference: WeakReference<Activity>) {
            activityWeakReference.get()?.startActivity(
                Intent(
                    activityWeakReference.get(),
                    AdditionalQuestActivity::class.java
                )
            )

        }
    }

    override fun setupView() {

        binding.toolbar.setBackListener { goBackPager() }
        binding.toolbar.setEndIconListener { HomeActivity.present(this,true) }


        binding.viewPager.adapter = AddQuestAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.apply {
            offscreenPageLimit=4
        }
        binding.pageIndicator.attachToPager(binding.viewPager)

    }

    fun goBackPager() {
        if (binding.viewPager.currentItem == 0)
            finish()
        else
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
    }

    fun movePager() {
        if (binding.viewPager.currentItem == binding.viewPager.adapter?.itemCount!! - 1)
            ThanksCompletingActivity.present(this@AdditionalQuestActivity)
        else
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
    }

    override fun bindViewModel() {

    }

    override fun getViewBinding() = ActivityAdditionalQuestBinding.inflate(layoutInflater)

    override fun onClick(view: View) {

    }
}