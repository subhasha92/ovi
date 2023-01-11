package org.ovi.feature.survey.view.activity

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivitySurveyBinding
import org.ovi.feature.survey.model.GetSurveyResponse
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.view.adapter.SurveyAdapter
import org.ovi.feature.survey.viewmodel.SurveyViewModel
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone
import org.ovi.widget.OviSnackBar

const val SURVEY_ID = "survey_id"
const val EVENT_ID = "event_id"
const val SURVEY_TYPE = "survey_type"

class SurveyActivity : BaseActivity<ActivitySurveyBinding>() {

    companion object {
        fun present(context: Context, surveyID: String, id: Int?, value: String) {
            context.startActivity(
                Intent(context, SurveyActivity::class.java)
                    .putExtra(SURVEY_ID, surveyID)
                    .putExtra(EVENT_ID,id)
                    .putExtra(SURVEY_TYPE,value)
            )
        }
    }

    private var data: GetSurveyResponse? = null
    private val vm: SurveyViewModel by viewModel()

    private val surveyID by lazy { intent.getStringExtra(SURVEY_ID) }
    private val eventID by lazy { intent.getIntExtra(EVENT_ID,-1) }
    private val surveyType by lazy { intent.getStringExtra(SURVEY_TYPE) }

    override fun setupView() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getSurvey(surveyID!!)
        binding.toolbar.setEndIconListener { finish() }
    }

    private fun setupViewPager(list: List<QuestionsItem?>) {
//        Log.e(TAG, "setupViewPager: setting up")
        binding.viewPager.apply {
            adapter = SurveyAdapter(supportFragmentManager, lifecycle, list)
            offscreenPageLimit = list.size
            isUserInputEnabled = false
            isSaveEnabled = false
        }
        binding.toolbar.toolbarTitle = data?.data?.name.toString()
    }



    fun movePager() {
        if (binding.viewPager.currentItem == binding.viewPager.adapter?.itemCount!! - 1)
            ThanksForSurveyActivity.present(this)
        else
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
    }

    fun goBackPager() {
        if (binding.viewPager.currentItem == 0)
            finish()
        else
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
    }

    fun submitResponse(request: SubmitSurveyRequest) {
        request.surveyType=surveyType
        request.eventId=eventID.toString()
        vm.submitSurvey(surveyID!!, request)
    }

    fun isLast(position: Int): Boolean {
        return data?.data?.questions?.size == position
    }

    override fun bindViewModel() {
        bindSubmit()
        vm.survey bindTo {
            when (it) {
                is ResultOf.Empty -> {

                }
                is ResultOf.Failure -> {
//                    Log.e(TAG, "bindViewModel failure: ${it.message}")
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
//                    Log.e(TAG, "bindViewModel: ${it.value}")
                    data = it.value
                    val sorted = it.value.data?.questions?.sortedBy { it?.position }
                    if (it.value.data?.questions?.isNotEmpty()==true)
                    setupViewPager(sorted!!)
                    else
                        dialogBoxForNoQuestions()

                }
            }
        }
    }

    private fun bindSubmit() {
        vm.submitSurvey bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    movePager()
                }
            }
        }
    }

    fun getQuestionData(position: Int): QuestionsItem? {
        return data?.data?.questions?.get(position)
    }

    override fun getViewBinding() = ActivitySurveyBinding.inflate(layoutInflater)

    override fun onClick(view: View) {

    }

    private fun dialogBoxForNoQuestions(){
        with(Dialog(this)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.custom_not_eligible_dialogue)

            val tvOkay = findViewById<AppCompatTextView>(R.id.tvOkay)
            val subTitle = findViewById<AppCompatTextView>(R.id.tvSubTitle)

            subTitle.text=getString(R.string.currently_no_question_are_available)

            tvOkay.setOnClickListener {
                dismiss()
                finish()
            }

            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }
}



enum class SurveyType(val value:String){
    PRE("pre"),
    POST("post")
}