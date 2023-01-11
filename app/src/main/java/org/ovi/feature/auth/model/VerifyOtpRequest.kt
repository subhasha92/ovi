package org.ovi.feature.auth.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VerifyOtpRequest(

	@Json(name="phone")
	val phone: String? = null,

	@Json(name="otp")
	val otp: Int? = null
)
