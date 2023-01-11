package org.ovi.feature.register.view.fragments

import android.view.View
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.DataUtil
import org.ovi.databinding.FragmentRegFinancialBinding
import org.ovi.feature.profile.model.AnswersItem

import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.feature.register.view.adapter.RegListAdapter
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.verticalView

class RegFinancialFragment:BaseFragment<FragmentRegFinancialBinding>() {
    override fun onClick(view: View) {

    }

    override fun getViewBinding()=FragmentRegFinancialBinding.inflate(layoutInflater)

    override fun setupView() {


    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        with(binding.include){
            tvTitle.text=getString(R.string.do_you_feel_confident_in_your_abilities_to_live_independently_without_outside_financial_assistance)
            tvText.text=getString(R.string.this_information_will_help_us_when_we_evaluate_the_program)
            rvRecycle.apply {
                verticalView(context)
                adapter= RegListAdapter(::callback, DataUtil.getYesNo())
                addItemDecoration(HorzSpacingItemDecoration(12))
            }
        }
    }

    private fun callback(choice_id:Int?,text_value:String){
        val answer= AnswersItem(text_value = text_value, choice_id = choice_id, question_id = 9)
        (requireActivity() as RegisterActivity).callOnBoardResponeAPi(answer)
        (requireActivity() as RegisterActivity).movePager()
    }
    override fun bindViewModel() {

    }
}