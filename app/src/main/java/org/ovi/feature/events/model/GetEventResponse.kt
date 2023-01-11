package org.ovi.feature.events.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

import kotlinx.android.parcel.RawValue

@JsonClass(generateAdapter = true)
data class GetEventResponse(

	@Json(name="data")
	val data: Data2? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)
@JsonClass(generateAdapter = true)
data class Address(

	@Json(name="zipcode")
	val zipcode: Any? = null
)

@JsonClass(generateAdapter = true)
data class CreatedBy(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="deletedAt")
	val deletedAt:@RawValue Any? = null,

	@Json(name="user_id")
	val userId: Int? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="job_title")
	val jobTitle:@RawValue Any? = null,

	@Json(name="user")
	val user: User? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)


@JsonClass(generateAdapter = true)

data class RowsItem(

	@Json(name="gift")
	val gift: String? = null,

	@Json(name="address")
	val address: Address? = null,

	@Json(name="visibility")
	val visibility: String? = null,

	@Json(name="image_url")
	val imageUrl: String? = null,

	@Json(name="timezone")
	val timezone: String? = null,

	@Json(name="link")
	val link: String? = null,

	@Json(name="county")
	val county:@RawValue Any? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="project")
	val project:@RawValue Any? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="created_by")
	val createdBy: CreatedBy? = null,

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="age_from")
	val ageFrom:@RawValue Any? = null,

	@Json(name="deletedAt")
	val deletedAt:@RawValue Any? = null,

	@Json(name="project_id")
	val projectId:@RawValue Any? = null,

	@Json(name="event_date")
	val eventDate: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="age_to")
	val ageTo:@RawValue Any? = null,

	@Json(name="created_by_id")
	val createdById: Int? = null,

	@Json(name="communication")
	val communication:@RawValue List<Any?>? = null,

	@Json(name="status")
	val status: String? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null,

	@Json(name="participants")
	val participants: List<ParticipantsItem?>? = null
)

@JsonClass(generateAdapter = true)
data class MemberUser(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="deletedAt")
	val deletedAt:@RawValue Any? = null,

	@Json(name="race")
	val race: String? = null,

	@Json(name="ethnicity")
	val ethnicity: String? = null,

	@Json(name="shirt_size")
	val shirtSize: String? = null,

	@Json(name="user_id")
	val userId: Int? = null,

	@Json(name="youth_council_name")
	val youthCouncilName: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="user")
	val user: User? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)

