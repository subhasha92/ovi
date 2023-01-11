package org.ovi.feature.events.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.ovi.feature.home.model.RowsItem

@JsonClass(generateAdapter = true)

data class GetUserEventResponse(

	@Json(name="data")
	val data: Data2? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class Data2(

	@Json(name="count")
	val count: Int? = null,

	@Json(name="rows")
	val rows: List<RowsItem?>? = null
)
