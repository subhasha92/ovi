package org.ovi.data.remote

import org.ovi.feature.notification.model.NotificationReadRequest
import org.ovi.feature.notification.model.NotificationReadResponse
import org.ovi.feature.notification.model.NotificationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface NotificationService {

    @GET("communication/notifications")
    suspend fun getNotification(@Query("page")page:Int?,@Query("pageSize")pageSize:Int?):NotificationResponse


    @PUT("communication/notifications/mark-read")
    suspend fun markNotification(@Body request: NotificationReadRequest):NotificationReadResponse


}