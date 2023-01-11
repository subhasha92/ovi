package org.ovi.feature.auth.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChangePasswordRequest(

	@Json(name="old_password")
	val oldPassword: String? = null,

	@Json(name="new_password")
	val newPassword: String? = null
)
