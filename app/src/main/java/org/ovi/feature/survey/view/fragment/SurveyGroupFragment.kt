package org.ovi.feature.survey.view.fragment

import android.view.View
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentSurveyGroupsBinding
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.getOviDrawable
import org.ovi.util.extensions.gone

class SurveyGroupFragment:BaseFragment<FragmentSurveyGroupsBinding>() {

    private var position:Int?=null

    private var data: QuestionsItem?=null

    fun initValues(questionsItem: QuestionsItem, position: Int){
        this.position=position
        data=questionsItem
    }

    override fun onClick(view: View) {
        when (view) {
            binding.incButton.tvPrevious -> {(requireActivity() as SurveyActivity).goBackPager()}
            binding.incButton.btnNext -> {(requireActivity() as SurveyActivity).movePager()}
        }
    }

    override fun getViewBinding()= FragmentSurveyGroupsBinding.inflate(layoutInflater)

    override fun setupView() {
        with(binding.incHeader){
            tvSno.text=(position?.plus(1)).toString()
            tvTitle.text=data?.title
        }

        if (data?.required == true)
            setEnableNext(false)
        if (position == 0)
            binding.incButton.tvPrevious.gone()
        binding.incButton.btnNext.setOnClickListener(onClickListener)
        binding.incButton.tvPrevious.setOnClickListener(onClickListener)

    }
    private fun setEnableNext(state: Boolean) {
        binding.incButton.btnNext.isEnabled = state
        if (state) {
            binding.incButton.btnNext.background =
                context?.getOviDrawable(R.drawable.bg_btn_rect_blue)
            binding.incButton.btnNext.setTextColor(context?.getOviColor(R.color.white_1000)!!)
        } else {
            binding.incButton.btnNext.background =
                context?.getOviDrawable(R.drawable.bg_grey_rounded_corner_4radius)
            binding.incButton.btnNext.setTextColor(context?.getOviColor(R.color.grey)!!)

        }

    }

    override fun bindViewModel() {

    }
}