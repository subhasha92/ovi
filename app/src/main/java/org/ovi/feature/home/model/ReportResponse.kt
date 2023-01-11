package org.ovi.feature.home.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReportResponse(

	@Json(name="data")
	val data: List<DataItem?>? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class DataItem(

	@Json(name="date")
	val date: String? = null,

	@Json(name="event_count")
	val event_count: String? = null
)
