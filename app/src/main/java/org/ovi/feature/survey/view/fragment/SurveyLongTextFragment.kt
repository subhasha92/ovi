package org.ovi.feature.survey.view.fragment

import android.text.InputFilter
import android.view.View
import androidx.core.widget.addTextChangedListener
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentSurveryLongTextBinding
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.model.Response
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.getOviDrawable
import org.ovi.util.extensions.hideSoftKeyboard
import org.ovi.util.extensions.showKeyboard

class SurveyLongTextFragment : BaseFragment<FragmentSurveryLongTextBinding>() {
    override fun onClick(view: View) {
        when (view) {
            binding.incButton.tvPrevious -> {
                (requireActivity() as SurveyActivity).goBackPager()
            }
            binding.incButton.btnNext -> {
                this.hideSoftKeyboard()
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
        if (binding.etInput.text?.isNotEmpty() == true) {
            val response = Response(
                textValue = binding.etInput.text.toString()
            )
//            if (binding.etRemark.text.isNotEmpty())
//                response.remarkValue = binding.etRemark.text.toString()
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

    private var position: Int? = null

    private var data: QuestionsItem? = null

    fun initValues(questionsItem: QuestionsItem, position: Int) {
        this.position = position
        data = questionsItem
    }

    override fun getViewBinding() = FragmentSurveryLongTextBinding.inflate(layoutInflater)

    override fun setupView() {
        binding.root.forceLayout()
        with(binding.incHeader) {
            tvSno.text = (position?.plus(1)).toString()
            tvTitle.text = data?.title
        }

        binding.etInput.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                this.showKeyboard(binding.etInput)
                binding.etInput.requestFocus()
            }

        }
//        binding.etInput.setOnClickListener {
//            binding.etInput.requestFocus()
//        }

        binding.etInput.filters =
            data?.properties?.maxRange?.let { InputFilter.LengthFilter(it) }
                ?.let { arrayOf<InputFilter>(it) }
        binding.tvCount.text =
            binding.etInput.text.toString().length.toString().plus("/${data?.properties?.maxRange}")

        if (data?.required == true)
            setEnableNext(false)
        else
            setEnableNext(true)


        binding.etInput.addTextChangedListener {
            if (data?.required == true)
                if (it?.isEmpty() == true)
                    setEnableNext(false)
                else
                    setEnableNext(true)
            else
                setEnableNext(true)

            binding.tvCount.text = it?.length.toString().plus("/${data?.properties?.maxRange}")
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