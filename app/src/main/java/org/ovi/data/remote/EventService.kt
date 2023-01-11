package org.ovi.data.remote

import org.ovi.feature.events.model.*
import org.ovi.feature.home.model.MyEventResponse
import org.ovi.feature.home.model.ReportResponse
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {

    @GET("event")
    suspend fun getEvents(): GetUserEventResponse

    @GET("member-user/events")
    suspend fun getUserEvents(
        @Query("invitation_status") invitation_status: String? = null,
        @Query("user_status") user_status: String? = null,
        @Query("is_upcoming") is_upcoming:Boolean?=true,
        @Query("unregistered") unregistered:Boolean?=null
    ): GetUserEventResponse

    @GET("member-user/event/{id}")
    suspend fun getParticularEvent(@Path("id") eventId: Int): ParticularEventResponse

    @PUT("member-user/event/{id}")
    suspend fun updateParticularEvent(
        @Path("id") eventId: Int,
        @Query("invitation_status") invitation_status: String
    ): UpdateEventResponse

    @PUT("member-user/event/{id}/register")
    suspend fun registerToEvent(@Path("id") eventId: Int):RegisterEventResponse

    @GET("member-user/{id}/report/events-attended")
    suspend fun getReport(@Path("id") userId: String): ReportResponse

    @GET("member-user/my-events")
    suspend fun getMyEvents():MyEventResponse

}