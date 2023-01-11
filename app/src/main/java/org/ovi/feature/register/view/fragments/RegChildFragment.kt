package org.ovi.feature.register.view.fragments

import android.view.View
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.DataUtil
import org.ovi.databinding.FragmentRegChildBinding
import org.ovi.feature.profile.model.AnswersItem
import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.feature.register.view.adapter.RegListAdapter
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.show
import org.ovi.util.extensions.verticalView

class RegChildFragment:BaseFragment<FragmentRegChildBinding>() {
    override fun onClick(view: View) {

        when (view) {
            binding.include.tvSkip -> {
                (requireActivity() as RegisterActivity).movePager()
            }
        }
    }

    override fun getViewBinding()=FragmentRegChildBinding.inflate(layoutInflater)

    override fun setupView() {


    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()

        with(binding.include){
            tvTitle.text=getString(R.string.have_you_given_birth_to_a_child_or_i_am_a_father_to_a_child_that_has_been_born_in_the_last_two_years)
            tvText.text=getString(R.string.this_information_will_help_us_when_we_evaluate_the_program)
            rvRecycle.apply {
                verticalView(context)
                adapter= RegListAdapter(::callback, DataUtil.getYesNo())
                addItemDecoration(HorzSpacingItemDecoration(12))
            }
            tvSkip.show()
            tvSkip.setOnClickListener(onClickListener)
        }
    }

    private fun callback(choice_id:Int?,text_value:String){
        val answer= AnswersItem(text_value = text_value, choice_id = choice_id, question_id = 7)
        (requireActivity() as RegisterActivity).callOnBoardResponeAPi(answer)
        (requireActivity() as RegisterActivity).movePager()
    }
    override fun bindViewModel() {

    }
}