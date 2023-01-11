package org.ovi.feature.events.domain

import org.ovi.feature.events.model.*
import org.ovi.feature.home.model.MyEventResponse
import org.ovi.feature.home.model.ReportResponse

interface IEventRepo {

    suspend fun getEvents(): GetUserEventResponse

    suspend fun getUserEvents(
        invitation_status: String?,
        user_status: String?,
       is_upcoming:Boolean?=true,
       unregistered:Boolean?=null
    ): GetUserEventResponse

    suspend fun getParticularEvent(eventId: Int): ParticularEventResponse

    suspend fun updateParticularEvent(eventId: Int,
                                      invitation_status: String):UpdateEventResponse

    suspend fun registerForEvent(eventId: Int):RegisterEventResponse

    suspend fun getReports(): ReportResponse
    suspend fun getMyEvents():MyEventResponse

}