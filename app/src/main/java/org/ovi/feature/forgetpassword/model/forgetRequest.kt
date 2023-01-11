package org.ovi.feature.forgetpassword.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForgetRequest(
	@Json(name="email")
	val email: String
)
