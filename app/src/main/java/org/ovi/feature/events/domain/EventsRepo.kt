package org.ovi.feature.events.domain

import org.ovi.data.pref.OVIPreferences
import org.ovi.data.remote.EventService
import org.ovi.feature.events.model.*
import org.ovi.feature.home.model.MyEventResponse
import org.ovi.feature.home.model.ReportResponse

class EventsRepo(
    private val eventService: EventService,
    private val oviPreferences: OVIPreferences
) : IEventRepo {



    override suspend fun getEvents(): GetUserEventResponse {
        return eventService.getEvents()
    }

    override suspend fun getUserEvents(
        invitation_status: String?,
        user_status: String?,
        is_upcoming: Boolean?,
        unregistered: Boolean?
    ): GetUserEventResponse {
        return eventService.getUserEvents(invitation_status,user_status,is_upcoming,unregistered)
    }

    override suspend fun getParticularEvent(eventId: Int): ParticularEventResponse {
        return eventService.getParticularEvent(eventId)
    }

    override suspend fun updateParticularEvent(
        eventId: Int,
        invitation_status: String
    ): UpdateEventResponse {
        return eventService.updateParticularEvent(eventId,invitation_status)
    }

    override suspend fun registerForEvent(eventId: Int): RegisterEventResponse {
        return eventService.registerToEvent(eventId)
    }

    override suspend fun getReports(): ReportResponse {
        return eventService.getReport(oviPreferences.id.toString())
    }

    override suspend fun getMyEvents(): MyEventResponse {
        return eventService.getMyEvents()
    }
}