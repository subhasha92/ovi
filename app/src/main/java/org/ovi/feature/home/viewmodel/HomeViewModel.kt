package org.ovi.feature.home.viewmodel

import org.ovi.base.BaseViewModel
import org.ovi.feature.events.domain.IEventRepo
import org.ovi.feature.events.model.GetUserEventResponse
import org.ovi.feature.events.model.UserStatus
import org.ovi.network.ErrorHandler
import org.ovi.network.ResultOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ovi.feature.events.model.InvitationStatus
import org.ovi.feature.home.model.ReportResponse

class HomeViewModel(
    private val iEventRepo: IEventRepo
) : BaseViewModel() {

    private val _eventsListhome = MutableStateFlow<ResultOf<GetUserEventResponse>>(ResultOf.Empty())
    val eventsListHome: StateFlow<ResultOf<GetUserEventResponse>>
        get() = _eventsListhome


    private val _userEventsListHome = MutableStateFlow<ResultOf<GetUserEventResponse>>(ResultOf.Empty())
    val userEventsListHome: StateFlow<ResultOf<GetUserEventResponse>>
        get() = _userEventsListHome

 private val _reports = MutableStateFlow<ResultOf<ReportResponse>>(ResultOf.Empty())
    val reports: StateFlow<ResultOf<ReportResponse>>
        get() = _reports



    fun getEvents() {
        execute {
            _eventsListhome.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iEventRepo.getEvents()
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _eventsListhome.value = ResultOf.Success(it)
                    else
                        _eventsListhome.value = ResultOf.Failure(it.message)
                }
                .onFailure {
                    _eventsListhome.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it),it)
                }
            _eventsListhome.value = ResultOf.Progress(false)
        }
    }

    fun getUserEvents(
        invitation_status: String? =  InvitationStatus.ACCEPTED.value,
        user_status: String? = UserStatus.REGISTERED.value
    ) {
        execute {
            _eventsListhome.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iEventRepo.getUserEvents(invitation_status, user_status, is_upcoming = true)
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _eventsListhome.value = ResultOf.Success(it)
                    else
                        _eventsListhome.value = ResultOf.Failure(it.message)
                }
                .onFailure {
                    _eventsListhome.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it),it)
                }
            _eventsListhome.value = ResultOf.Progress(false)
        }
    }


    fun getUnregisteredEvent() {
        execute {
            _userEventsListHome.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iEventRepo.getUserEvents(null, null, true, unregistered = true)
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _userEventsListHome.value = ResultOf.Success(it)
                    else
                        _userEventsListHome.value = ResultOf.Failure(it.message)
                }
                .onFailure {
                    _userEventsListHome.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _userEventsListHome.value = ResultOf.Progress(false)
        }
    }

    fun getReports(){
        execute {
            _reports.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iEventRepo.getReports()
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _reports.value = ResultOf.Success(it)
                    else
                        _reports.value = ResultOf.Failure(it.message)
                }
                .onFailure {
                    _reports.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _reports.value = ResultOf.Progress(false)
        }
    }
}