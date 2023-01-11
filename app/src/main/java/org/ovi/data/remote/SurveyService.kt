package org.ovi.data.remote

import org.ovi.feature.survey.model.GetSurveyResponse
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.model.SubmitSurveyResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SurveyService {

    @GET("questionnaires/{id}/questions")
    suspend fun getSurvey(@Path(value = "id") id: String): GetSurveyResponse

    @POST("questionnaires/{id}/submit_response")
    suspend fun submitSurvey(
        @Path(value = "id") id: String,
        @Body request: SubmitSurveyRequest
    ): SubmitSurveyResponse
}