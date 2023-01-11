package org.ovi.feature.notification.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationReadRequest(

	@Json(name="ids")
	val ids: List<Int?>? = null
)
