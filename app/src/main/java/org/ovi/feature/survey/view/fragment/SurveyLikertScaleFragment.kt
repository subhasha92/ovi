package org.ovi.feature.survey.view.fragment

import android.view.View
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.SelectionModelWithId
import org.ovi.databinding.FragmentSurveyLikertScaleBinding
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.model.Response
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.feature.survey.view.adapter.LikertScaleAdapter
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.getOviDrawable
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.verticalView

class SurveyLikertScaleFragment : BaseFragment<FragmentSurveyLikertScaleBinding>() {
    private var position: Int? = null

    private var data: QuestionsItem? = null

    fun initValues(questionsItem: QuestionsItem, position: Int) {
        this.position = position
        data = questionsItem
    }

    var selectionId = ""
    var selectedText = ""

    override fun onClick(view: View) {
        when (view) {
            binding.incButton.tvPrevious -> {
                (requireActivity() as SurveyActivity).goBackPager()
            }
            binding.incButton.btnNext -> {
//                if (data?.remark == true) {
//                    if (binding.etRemark.text.isEmpty())
//                        showSnackBar("Enter the remarks", OviSnackBar.SnackType.FAILURE)
//                    else
//                        processNext()
//                } else {
                processNext()
//                }
            }

        }
    }

    fun processNext() {
        if (selectionId.isNotEmpty() || selectionId != "") {
            val response = Response(
                questionChoiceId = selectionId.toString(),
                textValue = selectedText.toString()
            )
//        if (binding.etRemark.text.isNotEmpty())
//            response.remarkValue = binding.etRemark.text.toString()
            val request = SubmitSurveyRequest(
                response = response,
                questionType = data?.type,
                timeTaken = 300,
                questionId = data?.id
            )
            (requireActivity() as SurveyActivity).submitResponse(request)
        } else
            (requireActivity() as SurveyActivity).movePager()
    }

    override fun getViewBinding() = FragmentSurveyLikertScaleBinding.inflate(layoutInflater)

    override fun setupView() {
        with(binding.incHeader) {
            tvSno.text = (position?.plus(1)).toString()
            tvTitle.text = data?.title
        }

        binding.rvLikertScale.apply {
            verticalView(requireContext())
            addItemDecoration(HorzSpacingItemDecoration(0))
            adapter =
                data?.properties?.choices?.map {
                    SelectionModelWithId(
                        it?.label.toString(),
                        false,
                        it?.id.toString()
                    )
                }
                    ?.let { LikertScaleAdapter(it, ::callBack) }

        }

        if (data?.required == true)
            setEnableNext(false)
        else
            setEnableNext(true)

        if (position == 0)
            binding.incButton.tvPrevious.gone()
        binding.incButton.btnNext.setOnClickListener(onClickListener)
        binding.incButton.tvPrevious.setOnClickListener(onClickListener)
    }

    private fun callBack(state: Boolean, questionId: String, textValue: String) {
        setEnableNext(state)
        selectionId = questionId
        selectedText = textValue
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