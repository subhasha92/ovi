package org.ovi.feature.auth.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okio.Buffer
import org.ovi.base.BaseViewModel
import org.ovi.di.convertResponse
import org.ovi.feature.auth.domain.IAuthRepo
import org.ovi.feature.auth.model.*
import org.ovi.feature.forgetpassword.model.ForgetFailedResponse
import org.ovi.feature.forgetpassword.model.ForgetRequest
import org.ovi.feature.forgetpassword.model.ResetRequest
import org.ovi.network.ErrorHandler
import org.ovi.network.ResultOf
import org.ovi.util.extensions.toJsonString
import retrofit2.Response

class AuthViewModel(private val authRepo: IAuthRepo) : BaseViewModel() {

    private val _login = MutableStateFlow<ResultOf<LoginResponse>>(ResultOf.Empty())
    val login: StateFlow<ResultOf<LoginResponse>>
        get() = _login

    private val _forget = MutableStateFlow<ResultOf<LoginResponse>>(ResultOf.Empty())
    val forget: StateFlow<ResultOf<LoginResponse>>
        get() = _forget

    private val _reset = MutableStateFlow<ResultOf<Response<Any?>>>(ResultOf.Empty())
    val reset: StateFlow<ResultOf<Response<Any?>>>
        get() = _reset


    private val _change = MutableStateFlow<ResultOf<ChangePasswordResponse>>(ResultOf.Empty())
    val change: StateFlow<ResultOf<ChangePasswordResponse>>
        get() = _change

    private val _getOtp = MutableStateFlow<ResultOf<LoginResponse>>(ResultOf.Empty())
    val getOtp: StateFlow<ResultOf<LoginResponse>>
        get() = _getOtp

    private val _verifyOtp = MutableStateFlow<ResultOf<LoginResponse>>(ResultOf.Empty())
    val verifyOtp: StateFlow<ResultOf<LoginResponse>>
        get() = _verifyOtp


    private val _delete = MutableStateFlow<ResultOf<LoginResponse>>(ResultOf.Empty())
    val delete: StateFlow<ResultOf<LoginResponse>>
        get() = _delete

    fun doLogin(loginRequest: LoginRequest) {
        execute {
            _login.value = ResultOf.Progress(true)
            kotlin.runCatching {
                authRepo.login(loginRequest)
            }.onSuccess {
                if (it.statusCode == 201)
                    if (it.data?.role == "admin") {
                        _login.value = ResultOf.Failure("Invalid Credentials")
                    } else
                        _login.value = ResultOf.Success(it)
                else _login.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _login.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _login.value = ResultOf.Progress(false)
        }
    }

    fun forgetPassword(forgetRequest: ForgetRequest) {
        execute {
            _forget.value = ResultOf.Progress(true)
            kotlin.runCatching {
                authRepo.forget(forgetRequest)
            }.onSuccess {
                if (it.statusCode == 201)
                    _forget.value = ResultOf.Success(it)
                else _forget.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _forget.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _forget.value = ResultOf.Progress(false)
        }
    }

    fun resetPassword(resetPassword: ResetRequest) {
        execute {
            _reset.value = ResultOf.Progress(true)
            kotlin.runCatching {
                authRepo.reset(resetPassword)
            }.onSuccess {
                if (it.code() == 201)
                    _reset.value = ResultOf.Success(it)
                else {
                    val failedResponse: ForgetFailedResponse = it.errorBody()?.source()?.let { it1 ->
                        convertResponse(ForgetFailedResponse::class.java,
                            it1
                        )
                    } as ForgetFailedResponse
                    _reset.value = ResultOf.Failure(failedResponse.message)
                }
            }.onFailure {
                _reset.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _reset.value = ResultOf.Progress(false)
        }

    }

    fun changePassword(request: ChangePasswordRequest) {
        execute {
            _change.value = ResultOf.Progress(true)
            kotlin.runCatching {
                authRepo.changePass(request)
            }.onSuccess {
                if (it.statusCode == 201)
                    _change.value = ResultOf.Success(it)
                else _change.value = ResultOf.Failure(it.toString())
            }.onFailure {
                _change.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _change.value = ResultOf.Progress(false)
        }
    }

    fun getOtp(request: GetMobileOtpRequest) {
        execute {
            _getOtp.value = ResultOf.Progress(true)
            kotlin.runCatching {
                authRepo.getOtp(request)
            }.onSuccess {
                if (it.statusCode == 201)
                    _getOtp.value = ResultOf.Success(it)
                else _getOtp.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _getOtp.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _getOtp.value = ResultOf.Progress(false)
        }
    }

    fun verifyOtp(request: VerifyOtpRequest) {
        execute {
            _verifyOtp.value = ResultOf.Progress(true)
            kotlin.runCatching {
                authRepo.verifyOtp(request)
            }.onSuccess {
                if (it.statusCode == 201)
                    _verifyOtp.value = ResultOf.Success(it)
                else _verifyOtp.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _verifyOtp.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _verifyOtp.value = ResultOf.Progress(false)
        }
    }

    fun delete() {
        execute {
            _delete.value = ResultOf.Progress(true)
            kotlin.runCatching {
                authRepo.delete()
            }.onSuccess {
                if (it.statusCode == 201)
                    _delete.value = ResultOf.Success(it)
                else _delete.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _delete.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _delete.value = ResultOf.Progress(false)
        }
    }


}