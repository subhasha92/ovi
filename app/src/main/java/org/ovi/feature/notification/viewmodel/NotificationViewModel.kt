package org.ovi.feature.notification.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ovi.base.BaseViewModel
import org.ovi.feature.notification.domain.INotificationRepo
import org.ovi.feature.notification.model.NotificationReadRequest
import org.ovi.feature.notification.model.NotificationReadResponse
import org.ovi.feature.notification.model.NotificationResponse
import org.ovi.network.ErrorHandler
import org.ovi.network.ResultOf

class NotificationViewModel(private val iNotificationRepo: INotificationRepo):BaseViewModel() {


    private val _noti=MutableStateFlow<ResultOf<NotificationResponse>>(ResultOf.Empty())
    val noti:StateFlow<ResultOf<NotificationResponse>>
    get() = _noti

    private val _read=MutableStateFlow<ResultOf<NotificationReadResponse>>(ResultOf.Empty())
    val read:StateFlow<ResultOf<NotificationReadResponse>>
    get() = _read


    fun getNotification(page:Int?,pageSize:Int?){
        execute {
            _noti.value=ResultOf.Progress(true)
            kotlin.runCatching {
                iNotificationRepo.getNotification(page, pageSize)
            }
                .onSuccess {
                    if (it.statusCode==201)
                        _noti.value=ResultOf.Success(it)
                    else
                        _noti.value=ResultOf.Failure(it.message)
                }
                .onFailure {
                    _noti.value=ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _noti.value=ResultOf.Progress(false)
        }
    }

    fun readNotification(request: NotificationReadRequest){
        execute {
            _read.value=ResultOf.Progress(true)
            kotlin.runCatching {
                iNotificationRepo.markRead(request)
            }
                .onSuccess {
                    if (it.statusCode==201)
                        _read.value=ResultOf.Success(it)
                    else
                        _read.value=ResultOf.Failure(it.message)
                }
                .onFailure {
                    _read.value=ResultOf.Failure(ErrorHandler.getErrorMessage(it))
                }
            _read.value=ResultOf.Progress(false)
        }
    }



}