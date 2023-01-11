package org.ovi.feature.survey.view.adapter

import android.content.ContentValues.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.ovi.feature.survey.model.QuestionType
import org.ovi.feature.survey.model.QuestionsItem
import org.ovi.feature.survey.view.fragment.*

class SurveyAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    val list: List<QuestionsItem?>,
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount() = list.size

    override fun createFragment(position: Int): Fragment {
//        Log.e(TAG, "createFragment: $position")
        when (list[position]?.type) {
            QuestionType.NUMBER.type -> {
                val survey = SurveyNumberFragment()
                list[position]?.let { survey.initValues(it, position) }
                return survey
            }
            QuestionType.MULTIPLECHOICE.type -> {
                val survey = SurveyRadioButtonFragment()
                list[position]?.let { survey.initValues(it, position) }
                return survey
            }
            QuestionType.YESORNO.type -> {
                val survey = SurveyYesNoFragment()
                list[position]?.let { survey.initValues(it, position) }
                return survey
            }
            QuestionType.LONGTEXT.type -> {
                val survey = SurveyLongTextFragment()
                list[position]?.let { survey.initValues(it, position) }
                return survey
            }
            QuestionType.SHORTTEXT.type -> {
                val survey = SurveyShortTextFragment()
                list[position]?.let { survey.initValues(it, position) }
                return survey
            }
            QuestionType.PHONENUMBER.type -> {
                val survey = SurveyNumberFragment()
                list[position]?.let { survey.initValues(it, position) }
                return survey
            }
            QuestionType.DATETIME.type -> {
                val survey = SurveyDateTimeFragment()
                list[position]?.let { survey.initValues(it, position) }
                return survey
            }
            QuestionType.CHECKBOX.type -> {
                val survey = SurveyCheckBoxFragment()
                list[position]?.let { survey.initValues(it, position) }
                return survey
            }
            QuestionType.STATEMENT.type -> {
                val survey = SurveyStatementFragment()
                list[position]?.let { survey.initValues(it, position) }
                return survey
            }
            QuestionType.OPINIONSCALE.type -> {
                val survey = SurveyLikertScaleFragment()
                list[position]?.let { survey.initValues(it, position) }
                return survey
            }
            QuestionType.TIME.type -> {
                val survey = SurveyDateTimeFragment()
                list[position]?.let { survey.initValues(it, position) }
                return survey
            }
            else -> {
                return SurveyNumberFragment()
            }
        }

    }
}