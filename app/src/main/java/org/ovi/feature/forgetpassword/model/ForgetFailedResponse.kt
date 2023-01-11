package org.ovi.feature.forgetpassword.model

import com.squareup.moshi.Json

data class ForgetFailedResponse(

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)
