package org.ovi.feature.profile.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupsResponse(

	@Json(name="data")
	val data: DataGroup? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class DataGroup(

	@Json(name="count")
	val count: Int? = null,

	@Json(name="rows")
	val rows: List<RowsItemGroup?>? = null
)

@JsonClass(generateAdapter = true)
data class CreatedByGroup(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="deletedAt")
	val deletedAt: Any? = null,

	@Json(name="user_id")
	val userId: Int? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="job_title")
	val jobTitle: String? = null,

	@Json(name="user")
	val user: UsersGroup? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class UsersGroup(

	@Json(name="birthday")
	val birthday: String? = null,

	@Json(name="role")
	val role: String? = null,

	@Json(name="address")
	val address: Address? = null,

	@Json(name="code")
	val code: Any? = null,

	@Json(name="gender")
	val gender: String? = null,

	@Json(name="image_url")
	val imageUrl: String? = null,

	@Json(name="otp_valid_upto")
	val otpValidUpto: Any? = null,

	@Json(name="county")
	val county: String? = null,

	@Json(name="other_gender")
	val otherGender: Any? = null,

	@Json(name="joined_on")
	val joinedOn: Any? = null,

	@Json(name="token")
	val token: String? = null,

	@Json(name="emails")
	val emails: List<String?>? = null,

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="deletedAt")
	val deletedAt: Any? = null,

	@Json(name="phone")
	val phone: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="status")
	val status: String? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class RowsItemGroup(

	@Json(name="included_roles")
	val includedRoles: List<String?>? = null,

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="deletedAt")
	val deletedAt: Any? = null,

	@Json(name="viewable_to_roles")
	val viewableToRoles: List<String?>? = null,

	@Json(name="image_url")
	val imageUrl: Any? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="description")
	val description: Any? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="created_by_id")
	val createdById: Int? = null,

	@Json(name="created_by")
	val createdBy: CreatedByGroup? = null,

	@Json(name="status")
	val status: String? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)


