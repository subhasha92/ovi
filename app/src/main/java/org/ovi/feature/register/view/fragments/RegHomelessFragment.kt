package org.ovi.feature.register.view.fragments

import android.view.View
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.DataUtil
import org.ovi.databinding.FragmentRegHomelessBinding
import org.ovi.feature.profile.model.AnswersItem

import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.feature.register.view.adapter.RegListAdapter
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.verticalView

class RegHomelessFragment:BaseFragment<FragmentRegHomelessBinding>() {
    override fun onClick(view: View) {

    }

    override fun getViewBinding()=FragmentRegHomelessBinding.inflate(layoutInflater)

    override fun setupView() {

    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        with(binding.include){
            tvTitle.text=getString(R.string.have_you_been_homeless_at_least_once_in_the_past_two_years)
            rvRecycle.apply {
                verticalView(context)
                adapter= RegListAdapter(::callback, DataUtil.getYesNo())
                addItemDecoration(HorzSpacingItemDecoration(12))
            }
        }


    }

    private fun callback(choice_id:Int?,text_value:String){
        val answer= AnswersItem(text_value = text_value, choice_id = choice_id, question_id = 4)
        (requireActivity() as RegisterActivity).callOnBoardResponeAPi(answer)
        (requireActivity() as RegisterActivity).movePager()
    }

    override fun bindViewModel() {

    }
}