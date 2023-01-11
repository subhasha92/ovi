package org.ovi.feature.register.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationResponse(

	@Json(name="data")
	val data: DataLocation? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class RowsItemLocation(

	@Json(name="country")
	val country: String? = null,

	@Json(name="city_zipcodes")
	val cityZipcodes: List<String?>? = null,

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="city")
	val city: String? = null,

	@Json(name="latitude")
	val latitude: String? = null,

	@Json(name="county")
	val county: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="state")
	val state: String? = null,

	@Json(name="longitude")
	val longitude: String? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class DataLocation(

	@Json(name="count")
	val count: Int? = null,

	@Json(name="rows")
	val rows: List<RowsItemLocation?>? = null
)
