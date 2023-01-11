package org.ovi.feature.survey.view.fragment

import android.view.View
import androidx.core.widget.addTextChangedListener
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentSurveyDateBinding
import org.ovi.feature.survey.model.QuestionType
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.model.Response
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.util.DatePicker
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.getOviDrawable
import org.ovi.util.extensions.gone

class SurveyDateTimeFragment : BaseFragment<FragmentSurveyDateBinding>() {

    private var position: Int? = null

    private var data: QuestionsItem? = null

    fun initValues(questionsItem: QuestionsItem, position: Int) {
        this.position = position
        data = questionsItem
    }

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
        if (binding.etInput.text.isNotEmpty()) {
            val response = Response()
            if (data?.type == QuestionType.DATETIME.type)
                response.dateValue = binding.etInput.text.toString()
            else
                response.textValue = binding.etInput.text.toString()
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

    override fun getViewBinding() = FragmentSurveyDateBinding.inflate(layoutInflater)

    override fun setupView() {
        with(binding.incHeader) {
            tvSno.text = (position?.plus(1)).toString()
            tvTitle.text = data?.title
        }

        if (data?.required == true) {
            setEnableNext(false)
            isRequired()
        } else
            setEnableNext(true)

        if (position == 0)
            binding.incButton.tvPrevious.gone()
        binding.incButton.btnNext.setOnClickListener(onClickListener)
        binding.incButton.tvPrevious.setOnClickListener(onClickListener)
    }

    private fun isRequired() {
        binding.etInput.editText?.addTextChangedListener {
            if (it?.isEmpty() == true)
                setEnableNext(false)
            else
                setEnableNext(true)
        }
        setEnableNext(false)
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


    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.etInput.setOnIconClickListener {
            binding.etInput.setError(null)
            if (data?.type == QuestionType.DATETIME.type)
                DatePicker.dateSetListener(binding.etInput, 2)
            else {
                DatePicker.timePicker(binding.etInput, true)
            }
        }
        binding.etInput.setEnable(false)
        if (data?.type == QuestionType.TIME.type) {
            binding.etInput.setEndIconDrawable(R.drawable.ic_time)
            binding.etInput.hint = "Select time"

        } else {
            binding.etInput.hint = "Select date and time"
        }
    }

    override fun bindViewModel() {
    }
}