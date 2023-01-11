package org.ovi.feature.survey.view.fragment

import android.annotation.SuppressLint
import android.view.View
import android.view.WindowManager
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.SelectionModelWithId
import org.ovi.databinding.FragmentSurveyRadiobuttonBinding
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.model.Response
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.feature.survey.view.adapter.RadioButtonListAdapter
import org.ovi.util.extensions.*
import org.ovi.widget.OviSnackBar


class SurveyRadioButtonFragment :
    BaseFragment<FragmentSurveyRadiobuttonBinding>() {

    private var position: Int? = null

    private var data: QuestionsItem? = null

    fun initValues(questionsItem: QuestionsItem, position: Int) {
        this.position = position
        data = questionsItem
    }

    var selectedChoice: String? = null

    override fun onClick(view: View) {
        when (view) {
            binding.incButton.tvPrevious -> {
                (requireActivity() as SurveyActivity).goBackPager()
            }
            binding.incButton.btnNext -> {
                if (selectedChoice != null) {
                    val response =
                        Response(questionChoiceId = selectedChoice)
                    val adapter = binding.rvCheckBox.adapter as RadioButtonListAdapter
                    if (adapter.otherSelected)
                        if (adapter.string.trim() == "")
                            showSnackBar("Other can't be blank", OviSnackBar.SnackType.VALIDATION)
                        else {
                            response.textValue = adapter.string
                            processNext(response)
                        }

                    else
                        processNext(response)

                } else
                    (requireActivity() as SurveyActivity).movePager()
            }

        }
    }

    private fun processNext(response: Response) {
        val request = SubmitSurveyRequest(
            response = response,
            questionType = data?.type,
            timeTaken = 300,
            questionId = data?.id
        )
        this.hideSoftKeyboard()
        (requireActivity() as SurveyActivity).submitResponse(request)
    }


    override fun getViewBinding() = FragmentSurveyRadiobuttonBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun setupView() {
        with(binding.incHeader) {
            tvSno.text = (position?.plus(1)).toString()
            tvTitle.text = data?.title
        }
        binding.incButton.btnNext.setOnClickListener(onClickListener)
        binding.incButton.tvPrevious.setOnClickListener(onClickListener)
        if (data?.required == true)
            setEnableNext(false, null)
        else
            setEnableNext(true, null)
        if (position == 0)
            binding.incButton.tvPrevious.gone()
    }


    private fun setEnableNext(state: Boolean, choice: String?) {
        selectedChoice = choice
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

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val choiceList =
            data?.properties?.choices?.map {
                SelectionModelWithId(
                    it?.label.toString(),
                    false,
                    it?.id.toString()
                )
            }
        val rAdapter=choiceList?.let { RadioButtonListAdapter(it, ::setEnableNext) }
//        rAdapter?.setHasStableIds(true)
        binding.rvCheckBox.apply {
            verticalView(requireContext())
            adapter = rAdapter
        }
        rAdapter?.notifyDataSetChanged()

    }

    override fun bindViewModel() {

    }
}