package org.ovi.feature.survey.view.fragment

import android.view.View
import com.github.dhaval2404.imagepicker.ImagePicker
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentSurveyUploadBinding
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.getOviDrawable
import org.ovi.util.extensions.gone

class SurveyUploadFragment : BaseFragment<FragmentSurveyUploadBinding>() {
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

    override fun getViewBinding() = FragmentSurveyUploadBinding.inflate(layoutInflater)

    override fun setupView() {
        with(binding.incHeader){
            tvSno.text=(position?.plus(1)).toString()
            tvTitle.text=data?.title
        }

        if (data?.required == true)
            setEnableNext(false)
        if (position == 0)
            binding.incButton.tvPrevious.gone()
        binding.btnUpload.setOnClickListener {
            ImagePicker.with(this)
                .maxResultSize(1080, 1080)
                .start()
        }
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