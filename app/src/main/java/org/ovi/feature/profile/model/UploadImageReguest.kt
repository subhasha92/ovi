package org.ovi.feature.profile.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UploadImageReguest(

	@Json(name="fileName")
	val fileName: String? = null
)
