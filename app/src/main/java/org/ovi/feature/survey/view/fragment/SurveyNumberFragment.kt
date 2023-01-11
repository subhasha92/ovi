package org.ovi.feature.survey.view.fragment

import android.view.View
import androidx.core.widget.addTextChangedListener
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentSurveyNumberBinding
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.model.Response
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.util.extensions.*

class SurveyNumberFragment : BaseFragment<FragmentSurveyNumberBinding>() {
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

    private fun processNext() {
        if (binding.etInput.text.isNotEmpty()) {
            val response = Response(numberValue = binding.etInput.text.toInt())
//            if (binding.etRemark.text.isNotEmpty())
//                response.remarkValue = binding.etRemark.text.toString()
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

    private var position: Int? = null

    private var data: QuestionsItem? = null

    fun initValues(questionsItem: QuestionsItem, position: Int) {
        this.position = position
        data = questionsItem
    }

    override fun getViewBinding() = FragmentSurveyNumberBinding.inflate(layoutInflater)

    override fun setupView() {
        binding.root.forceLayout()
        with(binding.incHeader) {
            tvSno.text = (position?.plus(1)).toString()
            tvTitle.text = data?.title
        }

        binding.etInput.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                this.showKeyboard(binding.etInput.editText)
                binding.etInput.editText?.requestFocus()
            }

        }

//        binding.etInput.editText?.setOnClickListener {
//            binding.etInput.editText?.focusAndShowKeyboard()
//        }

        if (data?.required == true)
            setEnableNext(false)
        else
            setEnableNext(true)
        if (position == 0)
            binding.incButton.tvPrevious.gone()
        binding.incButton.btnNext.setOnClickListener(onClickListener)
        binding.incButton.tvPrevious.setOnClickListener(onClickListener)

        binding.etInput.editText?.addTextChangedListener {
            if (it?.length!! > 0)
                setEnableNext(true)
        }

    }

    override fun bindViewModel() {

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

}