package org.ovi.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorDTO(@Json(name="error") val error: String?, @Json(name="errorCode") val errorCode: ErrorCodes)