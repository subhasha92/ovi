package org.ovi.feature.profile.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@JsonClass(generateAdapter = true)
@Parcelize
data class GetProfileResponse(

    @Json(name = "data")
    val data: DataProfile? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "statusCode")
    val statusCode: Int? = null
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Choice(

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "choice")
    val choice: String? = null
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class ResponsesItem(

    @Json(name = "text_value")
    val textValue: String? = null,

    @Json(name = "question")
    val question: Question? = null,

    @Json(name = "choice")
    val choice: Choice? = null
) : Parcelable


@JsonClass(generateAdapter = true)
@Parcelize
data class Agency(

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "county")
    val county: String? = null,

    @Json(name = "description")
    val description: @RawValue Any? = null
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class DataProfile(

    @Json(name = "birthday")
    val birthday: String? = null,

    @Json(name = "role")
    val role: String? = null,

    @Json(name = "address")
    val address: Address? = null,

    @Json(name = "code")
    val code: String? = null,

    @Json(name = "gender")
    val gender: String? = null,

    @Json(name = "image_url")
    val image_url: String? = null,

    @Json(name = "county")
    val county: String? = null,

    @Json(name = "events_accepted")
    val events_accepted: Int? = null,

    @Json(name = "otp")
    val otp: @RawValue Any? = null,

    @Json(name = "joined_on")
    val joined_on: String? = null,

    @Json(name = "token")
    val token: @RawValue Any? = null,

    @Json(name = "member_user")
    val member_user: MemberUser? = null,

    @Json(name = "events_attended")
    val events_attended: Int? = null,

    @Json(name = "emails")
    val emails: List<String?>? = null,

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "deletedAt")
    val deletedAt: @RawValue Any? = null,

    @Json(name = "phone")
    val phone: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "events_invited")
    val events_invited: Int? = null,

    @Json(name = "events_registered")
    val events_registered: Int? = null,

    @Json(name = "status")
    val status: String? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null,

    @Json(name = "user_groups")
    val user_groups: @RawValue List<UserGroup?>? = null
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class UserGroup(
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "group")
    val group: Group? = null
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Group(
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "image_url")
    val image_url: String? = null,


    ) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class MemberUser(

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "deletedAt")
    val deletedAt: @RawValue Any? = null,

    @Json(name = "race")
    val race: String? = null,

    @Json(name = "ethnicity")
    val ethnicity: String? = null,

    @Json(name = "agency")
    val agency: Agency? = null,

    @Json(name = "shirt_size")
    val shirt_size: String? = null,

    @Json(name = "user_id")
    val user_id: Int? = null,

    @Json(name = "agency_id")
    val agency_id: Int? = null,

    @Json(name = "youth_council_name")
    val youth_council_name: String? = null,

    @Json(name = "responses")
    val responses: List<ResponsesItem?>? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
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
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Question(

    @Json(name = "question")
    val question: String? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "position")
    val position: Int? = null,

    @Json(name = "type")
    val type: String? = null
) : Parcelable
