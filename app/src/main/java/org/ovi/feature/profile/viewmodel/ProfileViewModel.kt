package org.ovi.feature.profile.viewmodel

import org.ovi.base.BaseViewModel
import org.ovi.feature.profile.domain.IProfileRepo
import org.ovi.feature.profile.model.*
import org.ovi.feature.register.model.RegisterResponse
import org.ovi.network.ErrorHandler
import org.ovi.network.ResultOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.RequestBody
import retrofit2.Response

class ProfileViewModel(
    private val iProfileRepo: IProfileRepo,
    private val type: Int = -1
) : BaseViewModel() {

    private val _getProfile = MutableStateFlow<ResultOf<GetProfileResponse>>(ResultOf.Empty())
    val getProfile: StateFlow<ResultOf<GetProfileResponse>>
        get() = _getProfile

    private val _putProfile = MutableStateFlow<ResultOf<RegisterResponse>>(ResultOf.Empty())
    val putProfile: StateFlow<ResultOf<RegisterResponse>>
        get() = _putProfile

    private val _agency = MutableStateFlow<ResultOf<AgencyResponse>>(ResultOf.Empty())
    val agency: StateFlow<ResultOf<AgencyResponse>>
        get() = _agency

    private val _counties = MutableStateFlow<ResultOf<CountiesResponse>>(ResultOf.Empty())
    val counties: StateFlow<ResultOf<CountiesResponse>>
        get() = _counties


    init {
        if (type == -1)
            getProfile()
        else if (type == 1) {
            getAgency()
        }
    }

    fun getProfile() {
        execute {
            _getProfile.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iProfileRepo.getProfile()
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _getProfile.value = ResultOf.Success(it)
                    else
                        _getProfile.value = ResultOf.Failure(it.message)
                }
                .onFailure {
                    _getProfile.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _getProfile.value = ResultOf.Progress(false)
        }
    }

    fun putProfile(request: EditProfileRequest) {
        execute {
            _putProfile.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iProfileRepo.putProfile(request)
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _putProfile.value = ResultOf.Success(it)
                    else
                        _putProfile.value = ResultOf.Failure(it.message)
                }
                .onFailure {
                    _putProfile.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _putProfile.value = ResultOf.Progress(false)
        }

    }

    fun getAgency() {

        execute {
            _agency.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iProfileRepo.getAgency()
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _agency.value = ResultOf.Success(it)
                    else
                        _agency.value = ResultOf.Failure(it.message)
                }
                .onFailure {
                    _agency.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _agency.value = ResultOf.Progress(false)
        }

    }



}