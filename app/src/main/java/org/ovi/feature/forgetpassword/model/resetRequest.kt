package org.ovi.feature.forgetpassword.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResetRequest(

    @Json(name = "password")
    val password: String,

    @Json(name = "token")
    val token: String
)
