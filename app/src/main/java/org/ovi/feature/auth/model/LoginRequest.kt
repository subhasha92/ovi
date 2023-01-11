package org.ovi.feature.auth.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(

    @Json(name = "password")
    val password: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "device_token")
    val device_token: String?
)
