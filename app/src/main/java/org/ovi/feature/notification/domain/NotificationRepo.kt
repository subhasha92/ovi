package org.ovi.feature.notification.domain

import org.ovi.data.pref.OVIPreferences
import org.ovi.data.remote.NotificationService
import org.ovi.feature.notification.model.NotificationReadRequest
import org.ovi.feature.notification.model.NotificationReadResponse
import org.ovi.feature.notification.model.NotificationResponse

class NotificationRepo(
    private val notificationService: NotificationService,
    private val oviPreferences: OVIPreferences
) : INotificationRepo {
    override suspend fun getNotification(page:Int?,pageSize:Int?): NotificationResponse {
        return notificationService.getNotification(page,pageSize)
    }

    override suspend fun markRead(request: NotificationReadRequest): NotificationReadResponse {
        return notificationService.markNotification(request)
    }
}