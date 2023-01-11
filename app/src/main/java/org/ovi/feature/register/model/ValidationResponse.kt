package org.ovi.feature.register.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValidationResponse(

    @Json(name = "data")
    val data: Data? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "statusCode")
    val statusCode: Int? = null
)


@JsonClass(generateAdapter = true)
data class Data(

    @Json(name = "exists")
    val exists: Boolean? = null,

    @Json(name = "duplicateEmails")
    val duplicateEmails: List<String?>? = null,

    @Json(name = "phone_exists")
    val phone_exists: Boolean? = null
)
