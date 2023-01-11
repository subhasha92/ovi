package org.ovi.feature.survey.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ovi.base.BaseViewModel
import org.ovi.feature.survey.domain.ISurveyRepo
import org.ovi.feature.survey.model.GetSurveyResponse
import org.ovi.feature.survey.model.SubmitSurveyRequest
import org.ovi.feature.survey.model.SubmitSurveyResponse
import org.ovi.network.ErrorHandler
import org.ovi.network.ResultOf

class SurveyViewModel(private val iSurveyRepo: ISurveyRepo) : BaseViewModel() {

    private val _survey = MutableStateFlow<ResultOf<GetSurveyResponse>>(ResultOf.Empty())
    val survey: StateFlow<ResultOf<GetSurveyResponse>>
        get() = _survey

    private val _submitSurvey = MutableStateFlow<ResultOf<SubmitSurveyResponse>>(ResultOf.Empty())
    val submitSurvey: StateFlow<ResultOf<SubmitSurveyResponse>>
        get() = _submitSurvey

    fun getSurvey(id:String) {
        execute {
            _survey.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iSurveyRepo.getSurvey(id)
            }.onSuccess {
                if (it.statusCode == 200)
                    _survey.value = ResultOf.Success(it)
                else _survey.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _survey.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _survey.value = ResultOf.Progress(false)
        }
    }

    fun submitSurvey(id:String,request: SubmitSurveyRequest) {
        execute {
            _submitSurvey.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iSurveyRepo.submitSurvey(id,request)
            }.onSuccess {
                if (it.statusCode == 201)
                    _submitSurvey.value = ResultOf.Success(it)
                else _submitSurvey.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _submitSurvey.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _submitSurvey.value = ResultOf.Progress(false)
        }
    }

}