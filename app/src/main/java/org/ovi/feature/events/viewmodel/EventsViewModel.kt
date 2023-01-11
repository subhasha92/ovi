package org.ovi.feature.events.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import org.ovi.base.BaseViewModel
import org.ovi.feature.events.domain.IEventRepo
import org.ovi.feature.events.model.*
import org.ovi.network.ErrorHandler
import org.ovi.network.ResultOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ovi.feature.home.model.MyEventResponse

class EventsViewModel(
    private val iEventRepo: IEventRepo,
    private val type: Int = -1
) : BaseViewModel() {

    private val _eventsList = MutableStateFlow<ResultOf<GetUserEventResponse>>(ResultOf.Empty())
    val eventsList: StateFlow<ResultOf<GetUserEventResponse>>
        get() = _eventsList

    private val _userEventsList = MutableStateFlow<ResultOf<GetUserEventResponse>>(ResultOf.Empty())
    val userEventsList: StateFlow<ResultOf<GetUserEventResponse>>
        get() = _userEventsList

    private val _registeredEvents =
        MutableStateFlow<ResultOf<GetUserEventResponse>>(ResultOf.Empty())
    val registeredEvents: StateFlow<ResultOf<GetUserEventResponse>>
        get() = _registeredEvents

    private val _particularEvent =
        MutableStateFlow<ResultOf<ParticularEventResponse>>(ResultOf.Empty())
    val particularEvent: StateFlow<ResultOf<ParticularEventResponse>>
        get() = _particularEvent

    private val _updateEvent = MutableStateFlow<ResultOf<UpdateEventResponse>>(ResultOf.Empty())
    val updateEvent: StateFlow<ResultOf<UpdateEventResponse>>
        get() = _updateEvent

    private val _registerFOREvent =
        MutableStateFlow<ResultOf<RegisterEventResponse>>(ResultOf.Empty())
    val registerFOREvent: StateFlow<ResultOf<RegisterEventResponse>>
        get() = _registerFOREvent


    private val _myEvents =
        MutableStateFlow<ResultOf<MyEventResponse>>(ResultOf.Empty())
    val myEvents: StateFlow<ResultOf<MyEventResponse>>
        get() = _myEvents


    init {
        when (type) {
            -1 -> {
//                getUserEvents()
//                getRegisteredEvents()
                getMyEvents()
            }
            1 -> {
                getUserEvents(null, null)
            }
        }

    }

    fun getUserEvents(
        invitation_status: String? = InvitationStatus.ACCEPTED.value,
        user_status: String? = UserStatus.REGISTERED.value
    ) {
        execute {
            _userEventsList.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iEventRepo.getUserEvents(invitation_status, user_status, is_upcoming = true)
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _userEventsList.value = ResultOf.Success(it)
                    else
                        _userEventsList.value = ResultOf.Failure(it.message, code = it.statusCode.toString())
                }
                .onFailure {
                    _userEventsList.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _userEventsList.value = ResultOf.Progress(false)
        }
    }

    fun getRegisteredEvents(
        invitation_status: String? = null,
        user_status: String? = UserStatus.REGISTERED.value
    ) {
        execute {
            _registeredEvents.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iEventRepo.getUserEvents(invitation_status, user_status, is_upcoming = true)
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _registeredEvents.value = ResultOf.Success(it)
                    else
                        _registeredEvents.value = ResultOf.Failure(it.message, code = it.statusCode.toString())
                }
                .onFailure {
                    _registeredEvents.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _registeredEvents.value = ResultOf.Progress(false)
        }
    }

    fun getUnregisteredEvent(){
        execute {
            _eventsList.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iEventRepo.getUserEvents(null, null,true, unregistered = true)
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _eventsList.value = ResultOf.Success(it)
                    else
                        _eventsList.value = ResultOf.Failure(it.message)
                }
                .onFailure {
                    _eventsList.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _eventsList.value = ResultOf.Progress(false)
        }
    }

    fun getParticularEvent(eventId: Int) {
        execute {
            _particularEvent.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iEventRepo.getParticularEvent(eventId)
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _particularEvent.value = ResultOf.Success(it)
                    else
                        _particularEvent.value = ResultOf.Failure(it.message)
                }
                .onFailure {
                    _particularEvent.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _particularEvent.value = ResultOf.Progress(false)
        }
    }

    fun registerForEvents(eventId: Int) {

        execute {
            _registerFOREvent.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iEventRepo.registerForEvent(eventId)
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _registerFOREvent.value = ResultOf.Success(it)
                    else {
                        _registerFOREvent.value = ResultOf.Failure(it.message, code = it.statusCode.toString())
//                        Log.e(TAG, "registerForEvents: ${it.statusCode}", )
                    }
                }
                .onFailure {
                    _registerFOREvent.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it), code = it.localizedMessage)
//                    Log.e(TAG, "registerForEvents: ${it.localizedMessage} : ${it.message}", )
                }
            _registerFOREvent.value = ResultOf.Progress(false)
        }
    }

    fun updateEvent(eventId: Int, invitation_status: InvitationStatus) {
        execute {
            _updateEvent.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iEventRepo.updateParticularEvent(eventId, invitation_status.value)
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _updateEvent.value = ResultOf.Success(it)
                    else
                        _updateEvent.value = ResultOf.Failure(it.message, code = it.statusCode.toString())
                }
                .onFailure {
                    _updateEvent.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it), code = it.localizedMessage)
                }
            _updateEvent.value = ResultOf.Progress(false)
        }
    }


    fun getMyEvents(){
        execute {
            _myEvents.value = ResultOf.Progress(true)
            kotlin.runCatching {
                iEventRepo.getMyEvents()
            }
                .onSuccess {
                    if (it.statusCode == 201)
                        _myEvents.value = ResultOf.Success(it)
                    else
                        _myEvents.value = ResultOf.Failure(it.message)
                }
                .onFailure {
                    _myEvents.value = ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _myEvents.value = ResultOf.Progress(false)
        }
    }


}