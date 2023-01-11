package org.ovi.feature.survey.domain

import org.ovi.feature.survey.model.GetSurveyResponse
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.model.SubmitSurveyResponse

interface ISurveyRepo {

    suspend fun getSurvey(id:String):GetSurveyResponse
    suspend fun submitSurvey(id:String,request:SubmitSurveyRequest):SubmitSurveyResponse

}