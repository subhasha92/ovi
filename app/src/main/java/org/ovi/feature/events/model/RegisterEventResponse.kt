package org.ovi.feature.events.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterEventResponse(

    @Json(name="data")
	val data: Data? = null,

    @Json(name="message")
	val message: String? = null,

    @Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class Data(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="has_attended")
	val hasAttended: Boolean? = null,

	@Json(name="event_id")
	val eventId: Int? = null,

	@Json(name="invited_on")
	val invitedOn: Any? = null,

	@Json(name="invitation_status")
	val invitationStatus: String? = null,

	@Json(name="member_user_id")
	val memberUserId: Int? = null,

	@Json(name="registered_on")
	val registeredOn: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="status")
	val status: String? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)
