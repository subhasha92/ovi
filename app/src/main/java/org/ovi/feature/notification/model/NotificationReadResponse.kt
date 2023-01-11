package org.ovi.feature.notification.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationReadResponse(

	@Json(name="data")
	val data: List<Any?>? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)
