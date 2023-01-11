package org.ovi.feature.survey.view.fragment

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.Fragment
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.SelectionModelWithId
import org.ovi.databinding.FragmentSurveyYesNoBinding
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.model.Response
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.feature.survey.view.adapter.CheckBoxListAdapter
import org.ovi.feature.survey.view.adapter.CheckBoxSelectionType
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.getOviDrawable
import org.ovi.util.extensions.hideSoftKeyboard
import org.ovi.util.extensions.verticalView

class SurveyYesNoFragment : BaseFragment<FragmentSurveyYesNoBinding>() {
    override fun onClick(view: View) {
        when (view) {
            binding.incButton.tvPrevious -> {
                (requireActivity() as SurveyActivity).goBackPager()
            }
            binding.incButton.btnNext -> {
                this.hideSoftKeyboard()
                if (textValueResponse != "") {
                    val response = Response(
                        questionChoiceId = textValueResponse
                    )

                    val submit = SubmitSurveyRequest(
                        questionId = data?.id,
                        timeTaken = 300,
                        response = response,
                        questionType = data?.type
                    )
                    (requireActivity() as SurveyActivity).submitResponse(submit)
                } else
                    (requireActivity() as SurveyActivity).movePager()
            }
        }
    }

    var textValueResponse = ""
    private var position: Int? = null

    private var data: QuestionsItem? = null

    fun initValues(questionsItem: QuestionsItem, position: Int) {
        this.position = position
        data = questionsItem
    }

    override fun getViewBinding() = FragmentSurveyYesNoBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun setupView() {
        with(binding.incHeader) {
            tvSno.text = (position!! + 1).toString()
            tvTitle.text = data?.title
        }
        if (data?.required == true)
            setEnableNext(false)
        else
            setEnableNext(true)

    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.rvCheckBox.apply {
            val list = data?.properties?.choices?.map {
                SelectionModelWithId(
                    it?.label.toString(),
                    false,
                    it?.id.toString()
                )
            }
            verticalView(requireContext())
            adapter = CheckBoxListAdapter(
                CheckBoxSelectionType.SINGLE,
                list!!,
                ::callBack
            )
        }

        binding.incButton.btnNext.setOnClickListener(onClickListener)
        binding.incButton.tvPrevious.setOnClickListener(onClickListener)
    }

    private fun callBack(state: Boolean, text_value: List<String>, questionId: List<String>) {
        textValueResponse =questionId[0]
        setEnableNext(state)
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