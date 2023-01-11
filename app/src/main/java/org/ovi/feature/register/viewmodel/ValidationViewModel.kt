package org.ovi.feature.register.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ovi.base.BaseViewModel
import org.ovi.feature.register.domain.IValidationRepo
import org.ovi.feature.register.model.RegisterRequest
import org.ovi.feature.register.model.ValidationResponse
import org.ovi.network.ErrorHandler
import org.ovi.network.ResultOf

class ValidationViewModel(private val iValidationRepo: IValidationRepo) : BaseViewModel() {

    private val _valid = MutableStateFlow<ResultOf<ValidationResponse>>(ResultOf.Empty())
    val valid: StateFlow<ResultOf<ValidationResponse>>
        get() = _valid


    fun validPhone(request: RegisterRequest){
        execute {
            _valid.value=ResultOf.Progress(true)
            kotlin.runCatching {
                iValidationRepo.validPhone(request)
            }
                .onSuccess {
                    if (it.statusCode==201)
                        _valid.value=ResultOf.Success(it)
                    else
                        _valid.value=ResultOf.Failure(it.message)
                }
                .onFailure {
                    _valid.value=ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _valid.value=ResultOf.Progress(false)

        }
    }

     fun validEmail(request: RegisterRequest){
        execute {
            _valid.value=ResultOf.Progress(true)
            kotlin.runCatching {
                iValidationRepo.validEmail(request)
            }
                .onSuccess {
                    if (it.statusCode==201)
                        _valid.value=ResultOf.Success(it)
                    else
                        _valid.value=ResultOf.Failure(it.message)
                }
                .onFailure {
                    _valid.value=ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _valid.value=ResultOf.Progress(false)

        }
    }





}