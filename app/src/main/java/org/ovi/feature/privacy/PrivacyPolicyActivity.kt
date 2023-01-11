package org.ovi.feature.privacy

import android.app.Activity
import android.content.Intent
import android.view.View
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityPrivacyPolicyBinding
import java.lang.ref.WeakReference

class PrivacyPolicyActivity : BaseActivity<ActivityPrivacyPolicyBinding>() {

companion object{
    fun present(activityWeakReference: WeakReference<Activity>)
    {
        activityWeakReference.get()?.startActivity(Intent(activityWeakReference.get(),PrivacyPolicyActivity::class.java))
    }
}

    override fun setupView() {

        binding.ivBack.setOnClickListener { finish() }

    }

    override fun bindViewModel() {

    }

    override fun getViewBinding() = ActivityPrivacyPolicyBinding.inflate(layoutInflater)

    override fun onClick(view: View) {

    }
}