package org.ovi.feature.auth.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(

	@Json(name="data")
	val data: DataLogin? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class DataLogin(

	@Json(name="birthday")
	val birthday: String? = null,

	@Json(name="role")
	val role: String? = null,

	@Json(name="address")
	val address: Address? = null,

	@Json(name="code")
	val code: String? = null,

	@Json(name="gender")
	val gender: String? = null,

	@Json(name="image_url")
	val imageUrl: String? = null,

	@Json(name="county")
	val county: String? = null,

	@Json(name="admin")
	val admin: Any? = null,

	@Json(name="otp")
	val otp: Any? = null,

	@Json(name="joined_on")
	val joinedOn: String? = null,

	@Json(name="member_user")
	val memberUser: MemberUserLogin? = null,

	@Json(name="token")
	val token: String? = null,

	@Json(name="emails")
	val emails: List<String?>? = null,

	@Json(name="deletedAt")
	val deletedAt: Any? = null,

	@Json(name="phone")
	val phone: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="status")
	val status: String? = null
)

@JsonClass(generateAdapter = true)
data class MemberUserLogin(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="deletedAt")
	val deletedAt: Any? = null,

	@Json(name="race")
	val race: String? = null,

	@Json(name="ethnicity")
	val ethnicity: String? = null,

	@Json(name="shirt_size")
	val shirtSize: String? = null,

	@Json(name="user_id")
	val userId: Int? = null,

	@Json(name="agency_id")
	val agencyId: Int? = null,

	@Json(name="youth_council_name")
	val youthCouncilName: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)

