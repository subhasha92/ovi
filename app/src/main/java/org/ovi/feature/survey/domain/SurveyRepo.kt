package org.ovi.feature.survey.domain

import org.ovi.data.pref.OVIPreferences
import org.ovi.data.remote.SurveyService
import org.ovi.feature.survey.model.GetSurveyResponse
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.model.SubmitSurveyResponse

class SurveyRepo(private val surveyService: SurveyService, private val oviPreferences: OVIPreferences ):ISurveyRepo {
    val id="6f198801-08a7-45ff-9241-e37229f4c6a7"
    override suspend fun getSurvey(id:String): GetSurveyResponse {
        return surveyService.getSurvey(id)
    }

    override suspend fun submitSurvey(id:String,request: SubmitSurveyRequest): SubmitSurveyResponse {
        return surveyService.submitSurvey(id,request)
    }

}