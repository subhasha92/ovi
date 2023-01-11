package org.ovi.feature.auth.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChangePasswordResponse(

	@Json(name="data")
	val data: Data? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class Data(

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
	val image_url: String? = null,

	@Json(name="county")
	val county: String? = null,

	@Json(name="admin")
	val admin: Any? = null,

	@Json(name="otp")
	val otp: Any? = null,

	@Json(name="joined_on")
	val joined_on: String? = null,

	@Json(name="member_user")
	val member_user: MemberUser? = null,

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

	@Json(name="exp")
	val exp: Int? = null,

	@Json(name="iat")
	val iat: Int? = null,

	@Json(name="status")
	val status: String? = null
)

@JsonClass(generateAdapter = true)
data class Address(

	@Json(name="zipcode")
	val zipcode: String? = null,

	@Json(name="city")
	val city: String? = null,

	@Json(name="street")
	val street: String? = null,

	@Json(name="county")
	val county: String? = null,

	@Json(name="state")
	val state: String? = null
)

@JsonClass(generateAdapter = true)
data class MemberUser(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="deletedAt")
	val deletedAt: Any? = null,

	@Json(name="race")
	val race: String? = null,

	@Json(name="ethnicity")
	val ethnicity: String? = null,

	@Json(name="shirt_size")
	val shirt_size: String? = null,

	@Json(name="user_id")
	val user_id: Int? = null,

	@Json(name="agency_id")
	val agency_id: Int? = null,

	@Json(name="youth_council_name")
	val youth_council_name: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)
