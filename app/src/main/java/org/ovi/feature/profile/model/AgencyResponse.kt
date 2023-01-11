package org.ovi.feature.profile.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AgencyResponse(

    @Json(name = "data")
    val data: DataAgency? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "statusCode")
    val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class CreatedBy(

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "deletedAt")
    val deletedAt: Any? = null,

    @Json(name = "user_id")
    val userId: Int? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "job_title")
    val jobTitle: String? = null,

    @Json(name = "user")
    val user: User? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class DataAgency(

    @Json(name = "count")
    val count: Int? = null,

    @Json(name = "rows")
    val rows: List<RowsItemAgency?>? = null
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
    val code: String? = null,

    @Json(name = "gender")
    val gender: String? = null,

    @Json(name = "image_url")
    val imageUrl: String? = null,

    @Json(name = "otp_valid_upto")
    val otpValidUpto: Any? = null,

    @Json(name = "county")
    val county: String? = null,

    @Json(name = "joined_on")
    val joinedOn: String? = null,

    @Json(name = "token")
    val token: Any? = null,

    @Json(name = "emails")
    val emails: List<Any?>? = null,

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
data class RowsItemAgency(

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "deletedAt")
    val deletedAt: Any? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "description")
    val description: Any? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "counties")
    val counties: Any? = null,

    @Json(name = "created_by_id")
    val createdById: Int? = null,

    @Json(name = "created_by")
    val createdBy: CreatedBy? = null,

    @Json(name = "status")
    val status: String? = null,

    @Json(name = "updatedAt")
    val updatedAt: String? = null
)
