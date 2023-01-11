package org.ovi.feature.register.view.fragments

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.DataUtil
import org.ovi.databinding.FragmentRegAdultLifeBinding
import org.ovi.feature.profile.model.AnswersItem
import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.feature.register.view.adapter.RegListAdapter
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.show
import org.ovi.util.extensions.verticalView

class RegAdultInLifeFragment : BaseFragment<FragmentRegAdultLifeBinding>() {
    override fun onClick(view: View) {

        when (view) {
            binding.include.tvSkip -> {
                (requireActivity() as RegisterActivity).movePager()
            }
        }

    }

    override fun getViewBinding() = FragmentRegAdultLifeBinding.inflate(layoutInflater)

    override fun setupView() {


    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        with(binding.include) {
            tvTitle.text =
                getString(R.string.do_you_have_at_least_one_adult_in_your_life_other_than_the_person_assigned_to_your_foster_care_or_il_case_that_you_can_go_to_for_advice_or_emotional_support)
            rvRecycle.apply {
                verticalView(context)
                adapter = RegListAdapter(::callback, DataUtil.getYesNo())
                addItemDecoration(HorzSpacingItemDecoration(12))
            }
            tvSkip.show()
            tvSkip.setOnClickListener(onClickListener)
        }
    }

    private fun callback(choice_id: Int?, text_value: String) {
        val answer = AnswersItem(text_value = text_value, choice_id = choice_id, question_id = 3)
        Log.e(TAG, "callback: $answer")
        (requireActivity() as RegisterActivity).callOnBoardResponeAPi(answer)
        (requireActivity() as RegisterActivity).movePager()
    }

    override fun bindViewModel() {

    }
}