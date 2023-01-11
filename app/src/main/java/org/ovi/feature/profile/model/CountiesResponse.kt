package org.ovi.feature.profile.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class CountiesResponse(

	@Json(name="data")
	val data: DataCounties? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class DataCounties(

	@Json(name="count")
	val count: Int? = null,

	@Json(name="rows")
	val rows: List<RowsItem?>? = null
)

@JsonClass(generateAdapter = true)
data class RowsItem(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="county_code")
	val countyCode: Any? = null,

	@Json(name="zipcodes")
	val zipcodes: Any? = null,

	@Json(name="county")
	val county: String? = null,

	@Json(name="state_abbr")
	val stateAbbr: Any? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="state")
	val state: String? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)
