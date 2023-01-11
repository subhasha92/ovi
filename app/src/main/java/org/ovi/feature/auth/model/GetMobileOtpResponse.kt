package org.ovi.feature.auth.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetMobileOtpResponse(

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)
