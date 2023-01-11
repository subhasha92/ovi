package org.ovi.feature.survey.view.fragment

import android.annotation.SuppressLint
import android.view.View
import androidx.core.widget.addTextChangedListener
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentSurveyShortTextBinding
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.model.Response
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.getOviDrawable
import org.ovi.util.extensions.hideSoftKeyboard
import org.ovi.util.extensions.showKeyboard

class SurveyShortTextFragment : BaseFragment<FragmentSurveyShortTextBinding>() {
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
            val response = Response(textValue = binding.etInput.text.toString())
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

    override fun getViewBinding() = FragmentSurveyShortTextBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun setupView() {

        with(binding.incHeader) {
            tvSno.text = (position!! + 1).toString()
            tvTitle.text = data?.title
        }

//        binding.etInput.forceLayout()

//        binding.etInput.editText?.setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus){
//                binding.etInput.editText?.requestFocus()
//            }
//        }

//        binding.etInput.toggleKeyboard(true)
        binding.etInput.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                this.showKeyboard(binding.etInput.editText)
                binding.etInput.editText?.requestFocus()
            }

        }

        data?.properties?.maxRange?.let { binding.etInput.setMaxLength(it) }
        binding.tvCount.text =
            binding.etInput.text.length.toString().plus("/${data?.properties?.maxRange}")
        setEnableNext(true)
        if (data?.required == true) {
            isRequired()
            setEnableNext(false)
        } else
            setEnableNext(true)

        binding.etInput.editText?.addTextChangedListener {
            binding.tvCount.text = it?.length.toString().plus("/${data?.properties?.maxRange}")
        }

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


    override fun bindViewModel() {

    }
}