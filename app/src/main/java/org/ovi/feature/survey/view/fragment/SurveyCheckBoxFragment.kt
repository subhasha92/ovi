package org.ovi.feature.survey.view.fragment

import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.SelectionModelWithId
import org.ovi.databinding.FragmentSurveyCheckboxBinding
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.model.Response
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.feature.survey.view.adapter.CheckBoxListAdapter
import org.ovi.feature.survey.view.adapter.CheckBoxSelectionType
import org.ovi.util.extensions.*
import org.ovi.widget.OviSnackBar

class SurveyCheckBoxFragment : BaseFragment<FragmentSurveyCheckboxBinding>() {


    override fun onClick(view: View) {
        when (view) {
            binding.incButton.tvPrevious -> {
                (requireActivity() as SurveyActivity).goBackPager()
            }
            binding.incButton.btnNext -> {
                if (selectedId.isNotEmpty()) {
                    val response = Response(
                        questionChoiceIds = selectedId
                    )
                    val adapter = binding.rvCheckBox.adapter as CheckBoxListAdapter
                    if (adapter.otherSelected) {
                        if (adapter.string == "" || adapter.string.isEmpty())
                            showSnackBar("Other can't be blank", OviSnackBar.SnackType.VALIDATION)
                        else {
                            response.textValue = adapter.string
                            processNext(response)
                        }

                    } else
                        processNext(response)

                } else
                    (requireActivity() as SurveyActivity).movePager()
            }
        }
    }

    fun processNext(response: Response) {
        val request = SubmitSurveyRequest(
            response = response,
            questionType = data?.type,
            timeTaken = 300,
            questionId = data?.id
        )
        this.hideSoftKeyboard()
        (requireActivity() as SurveyActivity).submitResponse(request)
    }

    private var position: Int? = null

    private var data: QuestionsItem? = null

    val selectedText = mutableListOf<String>()
    val selectedId = mutableListOf<String>()

    fun initValues(questionsItem: QuestionsItem, position: Int) {
        this.position = position
        data = questionsItem
    }

    override fun getViewBinding() = FragmentSurveyCheckboxBinding.inflate(layoutInflater)

    override fun setupView() {
        with(binding.incHeader) {
            tvSno.text = (position?.plus(1)).toString()
            tvTitle.text = data?.title
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


    override fun onFragmentCreated() {
        super.onFragmentCreated()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val checkAadapter=CheckBoxListAdapter(
            CheckBoxSelectionType.MULTIPLE,
            data?.properties?.choices?.map {
                SelectionModelWithId(
                    it?.label.toString(),
                    false,
                    it?.id.toString()
                )
            }!!,
            ::callBack
        )
        checkAadapter.setHasStableIds(true)
        binding.rvCheckBox.apply {
            verticalView(requireContext())
            adapter = checkAadapter
        }
        checkAadapter.notifyDataSetChanged()

    }

    private fun callBack(state: Boolean, text_value: List<String>, questionId: List<String>) {
        setEnableNext(state)
        selectedText.addAll(text_value)
        selectedId.addAll(questionId)
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