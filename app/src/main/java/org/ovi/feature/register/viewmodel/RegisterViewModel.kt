package org.ovi.feature.register.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import org.ovi.base.BaseViewModel
import org.ovi.feature.register.domain.IRegisterRepo
import org.ovi.feature.register.model.*
import org.ovi.network.ErrorHandler
import org.ovi.network.ResultOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel(private val iRegisterRepo: IRegisterRepo) : BaseViewModel() {

    private val _register = MutableStateFlow<ResultOf<RegisterResponse>>(ResultOf.Empty())
    val register: StateFlow<ResultOf<RegisterResponse>>
        get() = _register

    private val _onBoard = MutableStateFlow<ResultOf<OnBoardinResponse>>(ResultOf.Empty())
    val onBoard: StateFlow<ResultOf<OnBoardinResponse>>
        get() = _onBoard

    private val _putonBoard = MutableStateFlow<ResultOf<PutOnBoardResponse>>(ResultOf.Empty())
    val putonBoard: StateFlow<ResultOf<PutOnBoardResponse>>
        get() = _putonBoard

    private val _location = MutableStateFlow<ResultOf<LocationResponse>>(ResultOf.Empty())
    val location: StateFlow<ResultOf<LocationResponse>>
        get() = _location


    private val _zipcode = MutableStateFlow<String?>("")
    val zipcode: StateFlow<String?> = _zipcode

    fun setZip(zip: String) {
        _zipcode.value = zip
    }

    fun register(registerRequest: RegisterRequest) {
        execute {
//            Log.e(TAG, "register: $registerRequest" )
            _register.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iRegisterRepo.registerUser(registerRequest)
            }.onSuccess {
//                Log.e(TAG, "register: $it" )
                if (it.statusCode == 201)
                    _register.value = ResultOf.Success(it)
                else _register.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _register.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
//                Log.e(TAG, "register: ${it.cause}")
            }
            _register.value = ResultOf.Progress(false)
        }
    }

    fun getOnBoarding() {
        execute {
            _onBoard.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iRegisterRepo.getOnBoarding()
            }.onSuccess {
                if (it.statusCode == 201)
                    _onBoard.value = ResultOf.Success(it)
                else _onBoard.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _onBoard.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _onBoard.value = ResultOf.Progress(false)
        }
    }

    fun putOnBoarding(putOnBoardingResRequest: PutOnBoardingResRequest) {

        execute {
            _putonBoard.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iRegisterRepo.putOnBoard(putOnBoardingResRequest)
            }.onSuccess {
                if (it.statusCode == 201)
                    _putonBoard.value = ResultOf.Success(it)
                else _putonBoard.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _putonBoard.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _putonBoard.value = ResultOf.Progress(false)
        }
    }

    fun getLocation(zipCode: String? = null) {
        execute {
            _location.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iRegisterRepo.getLocation(zipCode)
            }.onSuccess {
                if (it.statusCode == 201)
                    _location.value = ResultOf.Success(it)
                else _location.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _location.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _location.value = ResultOf.Progress(false)
        }
    }


}