package org.ovi.feature.register.viewmodel

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ovi.base.BaseViewModel
import org.ovi.feature.profile.model.GroupsResponse
import org.ovi.feature.register.domain.IMasterRepo
import org.ovi.feature.register.model.LocationResponse
import org.ovi.feature.register.model.MasterDataResponse
import org.ovi.feature.register.model.ZipcodeResponse
import org.ovi.network.ErrorHandler
import org.ovi.network.ResultOf

class MasterViewModel(private val iMasterRepo: IMasterRepo) : BaseViewModel() {

    private val _race = MutableStateFlow<ResultOf<MasterDataResponse>>(ResultOf.Empty())
    val race: StateFlow<ResultOf<MasterDataResponse>>
        get() = _race

    private val _ethnicity = MutableStateFlow<ResultOf<MasterDataResponse>>(ResultOf.Empty())
    val ethnicity: StateFlow<ResultOf<MasterDataResponse>>
        get() = _ethnicity

    private val _masterData = MutableStateFlow<ResultOf<MasterDataResponse>>(ResultOf.Empty())
    val masterData: StateFlow<ResultOf<MasterDataResponse>>
        get() = _masterData

    private val _location = MutableStateFlow<ResultOf<LocationResponse>>(ResultOf.Empty())
    val location: StateFlow<ResultOf<LocationResponse>>
        get() = _location

    private val _ZipCodes = MutableStateFlow<ResultOf<ZipcodeResponse>>(ResultOf.Empty())
    val ZipCodes: StateFlow<ResultOf<ZipcodeResponse>>
        get() = _ZipCodes


    private val _counties = MutableStateFlow<ResultOf<ZipcodeResponse>>(ResultOf.Empty())
    val counties: StateFlow<ResultOf<ZipcodeResponse>>
        get() = _counties

    private val _groups = MutableStateFlow<ResultOf<GroupsResponse>>(ResultOf.Empty())
    val groups: StateFlow<ResultOf<GroupsResponse>>
        get() = _groups


    private var apiJob: Job? = null

    fun cancel() {
        apiJob?.cancel()
    }

    fun getRace() {
        execute {
            _race.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iMasterRepo.getRace()
            }.onSuccess {
                if (it.statusCode == 201)
                    _race.value = ResultOf.Success(it)
                else _race.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _race.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _race.value = ResultOf.Progress(false)
        }
    }

    fun getGroups() {
        execute {
            _groups.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iMasterRepo.getGroups()
            }.onSuccess {
                if (it.statusCode == 201)
                    _groups.value = ResultOf.Success(it)
                else _groups.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _groups.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _groups.value = ResultOf.Progress(false)
        }
    }

    fun getEthnicity() {
        execute {
            _ethnicity.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iMasterRepo.getEthnicity()
            }.onSuccess {
                if (it.statusCode == 201)
                    _ethnicity.value = ResultOf.Success(it)
                else _ethnicity.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _ethnicity.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _ethnicity.value = ResultOf.Progress(false)
        }
    }

    fun getMaster(type: String) {
        execute {
            _masterData.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iMasterRepo.getMasterOfType(type)
            }.onSuccess {
                if (it.statusCode == 201)
                    _masterData.value = ResultOf.Success(it)
                else _masterData.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _masterData.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _masterData.value = ResultOf.Progress(false)
        }
    }

    fun getLocation(zipCode: String? = null) {
        execute {
            _location.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iMasterRepo.getLocation(zipCode)
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

    fun getZips(zipCode: String? = null) {
        execute {
            _ZipCodes.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iMasterRepo.getZipCodes(zipCode)
            }.onSuccess {
                if (it.statusCode == 201)
                    _ZipCodes.value = ResultOf.Success(it)
                else _ZipCodes.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _ZipCodes.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _ZipCodes.value = ResultOf.Progress(false)
        }
    }

    fun getCounties(page: Int? = null, pageSize: Int? = 100, search: String? = null) {
        apiJob?.cancel()
        execute {
            delay(200)
        }
        apiJob = execute {
            _counties.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iMasterRepo.getCounties(page, pageSize, search)
            }.onSuccess {
                if (it.statusCode == 201)
                    _counties.value = ResultOf.Success(it)
                else _counties.value = ResultOf.Failure(it.message.toString())
            }.onFailure {
                _counties.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
            }
            _counties.value = ResultOf.Progress(false)
        }
    }

}