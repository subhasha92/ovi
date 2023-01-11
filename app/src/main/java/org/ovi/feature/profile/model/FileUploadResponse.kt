package org.ovi.feature.profile.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FileUploadResponse(

	@Json(name="data")
	val data: Data? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="statusCode")
	val statusCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class Fields(

	@Json(name="bucket")
	val bucket: String? = null,

	@Json(name="Policy")
	val policy: String? = null,

	@Json(name="X-Amz-Date")
	val xAmzDate: String? = null,

	@Json(name="X-Amz-Algorithm")
	val xAmzAlgorithm: String? = null,

	@Json(name="X-Amz-Signature")
	val xAmzSignature: String? = null,

	@Json(name="X-Amz-Security-Token")
	val xAmzSecurityToken: String? = null,

	@Json(name="X-Amz-Credential")
	val xAmzCredential: String? = null,

	@Json(name="acl")
	val acl: String? = null,

	@Json(name="signatureVersion")
	val signatureVersion: String? = null,

	@Json(name="key")
	val key: String? = null,

	@Json(name="Content-Type")
	val contentType: String? = null
)

@JsonClass(generateAdapter = true)
data class Data(

	@Json(name="fields")
	val fields: Fields? = null,

	@Json(name="get_url")
	val getUrl: String? = null,

	@Json(name="url")
	val url: String? = null
)
