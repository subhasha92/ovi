package org.ovi.feature.register.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(

	@Json(name="data")
	val data: DataRegister? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class DataRegister(

	@Json(name="birthday")
	val birthday: String? = null,

	@Json(name="address")
	val address: Address? = null,

	@Json(name="role")
	val role: String? = null,

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

	@Json(name="otp")
	val otp: Any? = null,

	@Json(name="joined_on")
	val joinedOn: String? = null,

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
