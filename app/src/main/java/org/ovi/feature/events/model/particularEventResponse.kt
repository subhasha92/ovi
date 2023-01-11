package org.ovi.feature.events.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.RawValue


@JsonClass(generateAdapter = true)
data class ParticipantsItem(

    @Json(name = "has_attended")
    val hasAttended: Boolean? = null,

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "invited_on")
    val invitedOn: @RawValue Any? = null,

    @Json(name = "event_id")
    val eventId: Int? = null,

    @Json(name = "invitation_status")
    val invitationStatus: String? = null,

    @Json(name = "member_user_id")
    val memberUserId: Int? = null,

    @Json(name = "registered_on")
    val registeredOn: String? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "status")
    val status: String? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class CreatedBy1(

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "deletedAt")
    val deletedAt: @RawValue Any? = null,

    @Json(name = "user_id")
    val userId: Int? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "job_title")
    val jobTitle: @RawValue Any? = null,

    @Json(name = "user")
    val user: User? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class User(

    @Json(name = "birthday")
    val birthday: @RawValue Any? = null,

    @Json(name = "role")
    val role: String? = null,

    @Json(name = "address")
    val address: @RawValue Any? = null,

    @Json(name = "code")
    val code: @RawValue Any? = null,

    @Json(name = "gender")
    val gender: @RawValue Any? = null,

    @Json(name = "image_url")
    val imageUrl: @RawValue Any? = null,

    @Json(name = "county")
    val county: @RawValue Any? = null,

    @Json(name = "joined_on")
    val joinedOn: @RawValue Any? = null,

    @Json(name = "token")
    val token: String? = null,

    @Json(name = "emails")
    val emails: List<String?>? = null,

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "deletedAt")
    val deletedAt: @RawValue Any? = null,

    @Json(name = "phone")
    val phone: @RawValue Any? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "status")
    val status: String? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class Data1(

    @Json(name = "gift")
    val gift: String? = null,

    @Json(name = "address")
    val address: Address? = null,

    @Json(name = "visibility")
    val visibility: String? = null,

    @Json(name = "image_url")
    val imageUrl: String? = null,

    @Json(name = "timezone")
    val timezone: String? = null,

    @Json(name = "link")
    val link: String? = null,

    @Json(name = "county")
    val county: @RawValue Any? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "project")
    val project: @RawValue Any? = null,

    @Json(name = "type")
    val type: String? = null,

    @Json(name = "created_by")
    val createdBy: CreatedBy? = null,

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "age_from")
    val ageFrom: @RawValue Any? = null,

    @Json(name = "deletedAt")
    val deletedAt: @RawValue Any? = null,

    @Json(name = "project_id")
    val projectId: @RawValue Any? = null,

    @Json(name = "event_start_date")
    val event_start_date: String? = null,

    @Json(name = "pre_survey_id")
    val pre_survey_id: String? = null,
    @Json(name = "post_survey_id")
    val post_survey_id: String? = null,
    @Json(name = "event_end_date")
    val event_end_date: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "age_to")
    val ageTo: @RawValue Any? = null,

    @Json(name = "created_by_id")
    val createdById: Int? = null,

    @Json(name = "status")
    val status: String? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null,

    @Json(name = "preQuizTaken")
    val preQuizTaken: Boolean? = null,

    @Json(name = "postQuizTaken")
    val postQuizTaken: Boolean? = null,

    @Json(name = "participants")
    val participants: List<ParticipantsItem?>? = null
)

@JsonClass(generateAdapter = true)
data class ParticularEventResponse(

    @Json(name = "data")
    val data: Data1? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "statusCode")
    val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class Address1(

    @Json(name = "zipcode")
    val zipcode: Int? = null
)
