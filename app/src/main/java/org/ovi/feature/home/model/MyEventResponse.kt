package org.ovi.feature.home.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyEventResponse(

    @Json(name = "data")
    val data: Data? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "statusCode")
    val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class Registered(

    @Json(name = "count")
    val count: Int? = null,

    @Json(name = "rows")
    val rows: List<RowsItem?>? = null
)

@JsonClass(generateAdapter = true)
data class Upcoming(

    @Json(name = "count")
    val count: Int? = null,

    @Json(name = "rows")
    val rows: List<RowsItem?>? = null
)

@JsonClass(generateAdapter = true)
data class UnRegistered(

    @Json(name = "count")
    val count: Int? = null,

    @Json(name = "rows")
    val rows: List<RowsItem?>? = null
)


@JsonClass(generateAdapter = true)
data class RowsItem(

    @Json(name = "gift")
    val gift: String? = null,

    @Json(name = "address")
    val address: Address? = null,

    @Json(name = "visibility")
    val visibility: String? = null,

    @Json(name = "image_url")
    val image_url: String? = null,

    @Json(name = "timezone")
    val timezone: String? = null,

    @Json(name = "link")
    val link: String? = null,

    @Json(name = "event_end_date")
    val event_end_date: String? = null,

    @Json(name = "pre_survey_id")
    val pre_survey_id: String? = null,
    @Json(name = "post_survey_id")
    val post_survey_id: String? = null,

    @Json(name = "county")
    val county: Any? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "project")
    val project: Project? = null,

    @Json(name = "type")
    val type: String? = null,

    @Json(name = "created_by")
    val created_by: CreatedBy? = null,

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "age_from")
    val age_from: Int? = null,

    @Json(name = "deletedAt")
    val deletedAt: Any? = null,

    @Json(name = "project_id")
    val project_id: Int? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "age_to")
    val age_to: Int? = null,

    @Json(name = "created_by_id")
    val created_by_id: Int? = null,

    @Json(name = "event_start_date")
    val event_start_date: String? = null,

    @Json(name = "status")
    val status: String? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null,

    @Json(name = "pre_survey")
    val pre_survey: PreSurvey? = null,

    @Json(name = "post_survey")
    val post_survey: PostSurvey? = null,

    @Json(name = "pre_survey_responses")
    val pre_survey_responses: List<UserResponse?>? = null,

     @Json(name = "post_survey_responses")
    val post_survey_responses: List<UserResponse?>? = null,

    @Json(name = "participants")
    val participants: List<ParticipantsItem?>? = null
)


@JsonClass(generateAdapter = true)
data class PreSurvey(
    @Json(name = "imageUrl") var imageUrl: String? = null,
    @Json(name = "id") var id: String? = null,
    @Json(name = "name") var name: String? = null,
    @Json(name = "description") var description: String? = null,
    @Json(name = "category") var category: Int? = null,
    @Json(name = "measure") var measure: String? = null,
    @Json(name = "minTime") var minTime: String? = null,
    @Json(name = "maxTime") var maxTime: String? = null,
    @Json(name = "links") var links: String? = null,
    @Json(name = "scoringCriteria") var scoringCriteria: String? = null,
    @Json(name = "notes") var notes: String? = null,
    @Json(name = "startDate") var startDate: String? = null,
    @Json(name = "endDate") var endDate: String? = null,
    @Json(name = "status") var status: String? = null,
    @Json(name = "questionCount") var questionCount: Int? = null,
    @Json(name = "createdAt") var createdAt: String? = null,
    @Json(name = "updatedAt") var updatedAt: String? = null,
    @Json(name = "deletedAt") var deletedAt: String? = null,
    @Json(name = "userResponses") var userResponses: List<UserResponse>? = null
)


@JsonClass(generateAdapter = true)
data class PostSurvey(

    @Json(name = "imageUrl") var imageUrl: String? = null,
    @Json(name = "id") var id: String? = null,
    @Json(name = "name") var name: String? = null,
    @Json(name = "description") var description: String? = null,
    @Json(name = "category") var category: Int? = null,
    @Json(name = "measure") var measure: String? = null,
    @Json(name = "minTime") var minTime: String? = null,
    @Json(name = "maxTime") var maxTime: String? = null,
    @Json(name = "links") var links: String? = null,
    @Json(name = "scoringCriteria") var scoringCriteria: String? = null,
    @Json(name = "notes") var notes: String? = null,
    @Json(name = "startDate") var startDate: String? = null,
    @Json(name = "endDate") var endDate: String? = null,
    @Json(name = "status") var status: String? = null,
    @Json(name = "questionCount") var questionCount: Int? = null,
    @Json(name = "createdAt") var createdAt: String? = null,
    @Json(name = "updatedAt") var updatedAt: String? = null,
    @Json(name = "deletedAt") var deletedAt: String? = null,
    @Json(name = "userResponses") var userResponses: List<UserResponse>? = null
)

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id")
    val id: String? = null
)

@JsonClass(generateAdapter = true)
data class Address(

    @Json(name = "zipcode")
    val zipcode: String? = null,

    @Json(name = "city")
    val city: String? = null,

    @Json(name = "street")
    val street: String? = null,

    @Json(name = "county")
    val county: String? = null,

    @Json(name = "state")
    val state: String? = null
)

@JsonClass(generateAdapter = true)
data class Project(

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "deletedAt")
    val deletedAt: Any? = null,

    @Json(name = "owner_id")
    val owner_id: Int? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "description")
    val description: Any? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "created_by_id")
    val created_by_id: Int? = null,

    @Json(name = "status")
    val status: String? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class Attended(

    @Json(name = "count")
    val count: Int? = null,

    @Json(name = "rows")
    val rows: List<RowsItem?>? = null
)

@JsonClass(generateAdapter = true)
data class User(

    @Json(name = "birthday")
    val birthday: String? = null,

    @Json(name = "role")
    val role: String? = null,

    @Json(name = "address")
    val address: Address? = null,

    @Json(name = "code")
    val code: Any? = null,

    @Json(name = "gender")
    val gender: String? = null,

    @Json(name = "image_url")
    val image_url: Any? = null,

    @Json(name = "otp_valid_upto")
    val otp_valid_upto: Any? = null,

    @Json(name = "county")
    val county: String? = null,

    @Json(name = "joined_on")
    val joined_on: Any? = null,

    @Json(name = "token")
    val token: Any? = null,

    @Json(name = "emails")
    val emails: List<String?>? = null,

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "deletedAt")
    val deletedAt: Any? = null,

    @Json(name = "phone")
    val phone: String? = null,

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
data class Data(

    @Json(name = "attended")
    val attended: Attended? = null,

    @Json(name = "invited")
    val invited: Invited? = null,

    @Json(name = "accepted")
    val accepted: Accepted? = null,

    @Json(name = "registered")
    val registered: Registered? = null,

    @Json(name = "unregistered")
    val unregistered: UnRegistered? = null,

    @Json(name = "upcoming")
    val upcoming: Upcoming? = null
)

@JsonClass(generateAdapter = true)
data class CreatedBy(

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "deletedAt")
    val deletedAt: Any? = null,

    @Json(name = "user_id")
    val user_id: Int? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "job_title")
    val job_title: String? = null,

    @Json(name = "user")
    val user: User? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class Invited(

    @Json(name = "count")
    val count: Int? = null,

    @Json(name = "rows")
    val rows: List<RowsItem?>? = null
)

@JsonClass(generateAdapter = true)
data class Accepted(

    @Json(name = "count")
    val count: Int? = null,

    @Json(name = "rows")
    val rows: List<RowsItem?>? = null
)

@JsonClass(generateAdapter = true)
data class ParticipantsItem(

    @Json(name = "has_attended")
    val has_attended: Boolean? = null,

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "invited_on")
    val invited_on: Any? = null,

    @Json(name = "event_id")
    val event_id: Int? = null,

    @Json(name = "invitation_status")
    val invitation_status: String? = null,

    @Json(name = "member_user_id")
    val member_user_id: Int? = null,

    @Json(name = "registered_on")
    val registered_on: String? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "status")
    val status: String? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null
)
