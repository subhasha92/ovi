package org.ovi.feature.survey.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityThanksForSurveyBinding
import org.ovi.feature.home.view.activity.HomeActivity

class ThanksForSurveyActivity:BaseActivity<ActivityThanksForSurveyBinding>() {

    companion object{
        fun present(context:Context){
            context.startActivity(Intent(context,ThanksForSurveyActivity::class.java))
        }
    }

    override fun setupView() {

        binding.btnHome.setOnClickListener(onClickListener)

    }

    override fun bindViewModel() {

    }

    override fun getViewBinding()=ActivityThanksForSurveyBinding.inflate(layoutInflater)

    override fun onClick(view: View) {
        when(view){
            binding.btnHome->{
                HomeActivity.present(this@ThanksForSurveyActivity)
            }
        }
    }
}