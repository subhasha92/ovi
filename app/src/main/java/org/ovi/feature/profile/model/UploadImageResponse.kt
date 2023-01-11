package org.ovi.feature.profile.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UploadImageResponse(

	@Json(name="data")
	val data: DataUpload? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class DataUpload(

	@Json(name="content_type")
	val contentType: String? = null,

	@Json(name="pre_signed_url")
	val preSignedUrl: String? = null,

	@Json(name="get_url")
	val getUrl: String? = null
)
