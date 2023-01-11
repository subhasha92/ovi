package org.ovi.feature.register.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MasterDataResponse(

	@Json(name="data")
	val data: DataMaster? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class DataMaster(

	@Json(name="count")
	val count: Int? = null,

	@Json(name="rows")
	val rows: List<RowsItemMaster?>? = null
)

@JsonClass(generateAdapter = true)
data class RowsItemMaster(

	@Json(name="createdAt")
	val createdAt: String? = null,

	@Json(name="value_type")
	val valueType: String? = null,

	@Json(name="code")
	val code: Any? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="value")
	val value: String? = null,

	@Json(name="updatedAt")
	val updatedAt: String? = null
)
