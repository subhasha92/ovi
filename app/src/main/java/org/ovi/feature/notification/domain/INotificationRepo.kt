package org.ovi.feature.notification.domain

import org.ovi.feature.notification.model.NotificationReadRequest
import org.ovi.feature.notification.model.NotificationReadResponse
import org.ovi.feature.notification.model.NotificationResponse

interface INotificationRepo {

    suspend fun getNotification(page:Int?,pageSize:Int?):NotificationResponse

    suspend fun markRead(request: NotificationReadRequest):NotificationReadResponse

}